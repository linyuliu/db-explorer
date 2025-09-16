<template>
  <div class="bg-white p-6 rounded-2xl shadow-lg border border-gray-200 flex flex-col" style="height: 70vh;">
    <h2 class="text-xl font-semibold mb-4 border-b pb-3 text-gray-800 flex items-center flex-shrink-0">
      <svg class="w-6 h-6 mr-2 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
      </svg>
      查询结果
    </h2>
    
    <!-- Error Display -->
    <div v-if="error" class="bg-red-50 border-l-4 border-red-500 text-red-800 p-4 rounded-lg my-4" role="alert">
      <p class="font-bold">发生错误</p>
      <p class="text-sm mt-1 break-all">{{ error }}</p>
    </div>
    
    <!-- Empty State -->
    <div v-if="!error && !databaseInfo && !loading" class="text-center text-gray-500 py-16 flex-grow flex flex-col justify-center items-center">
      <svg class="mx-auto h-20 w-20 text-gray-300" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 7v10c0 2.21 3.582 4 8 4s8-1.79 8-4V7M4 7c0 2.21 3.582 4 8 4s8-1.79 8-4M4 7c0-2.21 3.582-4 8-4s8 1.79 8 4m0 5c0 2.21-3.582 4-8 4s-8-1.79-8-4" />
      </svg>
      <p class="mt-4 text-base font-medium">请先配置并连接数据库</p>
      <p class="text-sm text-gray-400 mt-1">结果将在此处显示</p>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center text-gray-500 py-16 flex-grow flex flex-col justify-center items-center">
      <svg class="animate-spin h-12 w-12 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <p class="mt-4 text-base font-medium">正在连接数据库...</p>
    </div>

    <!-- Results Display -->
    <div v-if="databaseInfo" class="flex flex-col flex-grow min-h-0">
      <!-- Database Info Summary -->
      <div class="mb-4 p-3 bg-gray-50 rounded-lg border flex-shrink-0">
        <p class="text-sm text-gray-600">
          数据库 <strong class="font-semibold text-blue-700">{{ databaseInfo.databaseName }}</strong>
          <span v-if="databaseInfo.schema">
            的模式 <strong class="font-semibold text-blue-700">{{ databaseInfo.schema }}</strong>
          </span>
          中找到 <strong class="font-semibold text-blue-700">{{ tableCount }}</strong> 个表/视图
        </p>
        <p class="text-xs text-gray-500 mt-1">
          {{ databaseInfo.databaseType }} {{ databaseInfo.version }}
        </p>
      </div>
      
      <!-- Search Box -->
      <div class="relative mb-4 flex-shrink-0">
        <input 
          type="text" 
          v-model="searchTerm" 
          placeholder="搜索表名..." 
          class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 transition"
        >
        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
          </svg>
        </div>
      </div>

      <!-- Tables List -->
      <div class="overflow-y-auto flex-grow custom-scrollbar">
        <ul class="divide-y divide-gray-200">
          <li 
            v-for="table in filteredTables" 
            :key="table" 
            class="py-3 flex items-center hover:bg-gray-50 rounded-md px-2 -mx-2 transition-colors cursor-pointer"
            @click="selectTable(table)"
          >
            <svg class="h-5 w-5 text-gray-400 mr-3 flex-shrink-0" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M3 14h18m-9-4v8m-7 0h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z" />
            </svg>
            <span class="text-gray-800 font-medium select-all hover:text-blue-600">{{ table }}</span>
          </li>
        </ul>
        
        <!-- No Results -->
        <p v-if="filteredTables.length === 0 && tableCount > 0" class="text-center text-gray-500 mt-8">
          未找到匹配的表。
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ResultsPanel',
  props: {
    databaseInfo: {
      type: Object,
      default: null
    },
    error: {
      type: String,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['table-selected'],
  data() {
    return {
      searchTerm: ''
    }
  },
  computed: {
    tableCount() {
      return this.databaseInfo?.tables?.length || 0
    },
    filteredTables() {
      if (!this.databaseInfo?.tables) return []
      if (!this.searchTerm) return this.databaseInfo.tables
      
      return this.databaseInfo.tables.filter(table => 
        table.toLowerCase().includes(this.searchTerm.toLowerCase())
      )
    }
  },
  methods: {
    selectTable(tableName) {
      this.$emit('table-selected', tableName)
    }
  }
}
</script>