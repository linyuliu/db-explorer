<template>
  <div class="max-w-7xl mx-auto p-4 md:p-6 lg:p-8">
    <!-- Header -->
    <header class="text-center mb-8">
      <h1 class="text-3xl md:text-4xl font-bold text-gray-900 tracking-tight">
        数据库探索器
      </h1>
      <p class="text-lg text-gray-600 mt-2">
        支持多种数据库的专业数据库管理工具
      </p>
    </header>

    <div class="grid lg:grid-cols-12 gap-8">
      <!-- Left Panel: Connection Configuration -->
      <div class="lg:col-span-4">
        <ConnectionPanel 
          @connect="handleConnect" 
          @loading="setLoading"
          :disabled="loading"
        />
      </div>

      <!-- Right Panel: Results -->
      <div class="lg:col-span-8">
        <ResultsPanel 
          :database-info="databaseInfo"
          :error="error"
          :loading="loading"
          @table-selected="handleTableSelected"
        />
      </div>
    </div>

    <!-- Table Metadata Modal -->
    <TableMetadataModal 
      v-if="showMetadataModal"
      :table-metadata="selectedTableMetadata"
      :loading="metadataLoading"
      @close="closeMetadataModal"
    />
  </div>
</template>

<script>
import ConnectionPanel from './ConnectionPanel.vue'
import ResultsPanel from './ResultsPanel.vue'
import TableMetadataModal from './TableMetadataModal.vue'
import apiService from '../services/apiService'

export default {
  name: 'DatabaseExplorer',
  components: {
    ConnectionPanel,
    ResultsPanel,
    TableMetadataModal
  },
  data() {
    return {
      databaseInfo: null,
      error: null,
      loading: false,
      currentConnection: null,
      showMetadataModal: false,
      selectedTableMetadata: null,
      metadataLoading: false
    }
  },
  methods: {
    async handleConnect(connectionRequest) {
      this.loading = true
      this.error = null
      this.databaseInfo = null
      
      try {
        const response = await apiService.connect(connectionRequest)
        this.databaseInfo = response
        this.currentConnection = connectionRequest
        
        // Sort tables for better UX
        if (this.databaseInfo.tables) {
          this.databaseInfo.tables.sort()
        }
      } catch (error) {
        this.error = error.message || 'Connection failed'
        console.error('Connection error:', error)
      } finally {
        this.loading = false
      }
    },

    async handleTableSelected(tableName) {
      if (!this.currentConnection) return
      
      this.metadataLoading = true
      this.showMetadataModal = true
      this.selectedTableMetadata = null
      
      try {
        const metadata = await apiService.getTableMetadata(this.currentConnection, tableName)
        this.selectedTableMetadata = metadata
      } catch (error) {
        console.error('Failed to load table metadata:', error)
        this.error = 'Failed to load table metadata: ' + error.message
        this.closeMetadataModal()
      } finally {
        this.metadataLoading = false
      }
    },

    closeMetadataModal() {
      this.showMetadataModal = false
      this.selectedTableMetadata = null
      this.metadataLoading = false
    },

    setLoading(state) {
      this.loading = state
    }
  }
}
</script>