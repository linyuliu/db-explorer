<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-75 flex items-center justify-center p-4 z-50">
    <div class="bg-white rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-hidden">
      <!-- Header -->
      <div class="flex items-center justify-between p-6 border-b border-gray-200 bg-gray-50">
        <div>
          <h3 class="text-lg font-semibold text-gray-900">
            表元数据
          </h3>
          <p class="text-sm text-gray-600 mt-1" v-if="tableMetadata">
            {{ tableMetadata.tableName }}
          </p>
        </div>
        <button 
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </button>
      </div>

      <!-- Content -->
      <div class="p-6 overflow-y-auto custom-scrollbar" style="max-height: calc(90vh - 120px);">
        <!-- Loading State -->
        <div v-if="loading" class="text-center py-8">
          <svg class="animate-spin h-8 w-8 text-blue-600 mx-auto" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <p class="mt-2 text-gray-600">正在加载表元数据...</p>
        </div>

        <!-- Metadata Display -->
        <div v-else-if="tableMetadata" class="space-y-6">
          <!-- Basic Info -->
          <div class="bg-gray-50 rounded-lg p-4">
            <h4 class="font-medium text-gray-900 mb-3">基本信息</h4>
            <div class="grid grid-cols-2 gap-4 text-sm">
              <div>
                <span class="text-gray-500">表名:</span>
                <span class="ml-2 font-medium">{{ tableMetadata.tableName }}</span>
              </div>
              <div>
                <span class="text-gray-500">类型:</span>
                <span class="ml-2 font-medium">{{ tableMetadata.tableType || 'TABLE' }}</span>
              </div>
              <div v-if="tableMetadata.schema">
                <span class="text-gray-500">模式:</span>
                <span class="ml-2 font-medium">{{ tableMetadata.schema }}</span>
              </div>
              <div v-if="tableMetadata.rowCount !== null">
                <span class="text-gray-500">行数:</span>
                <span class="ml-2 font-medium">{{ tableMetadata.rowCount }}</span>
              </div>
            </div>
            <div v-if="tableMetadata.remarks" class="mt-3">
              <span class="text-gray-500">备注:</span>
              <p class="mt-1 text-sm text-gray-700">{{ tableMetadata.remarks }}</p>
            </div>
          </div>

          <!-- Columns -->
          <div>
            <h4 class="font-medium text-gray-900 mb-3 flex items-center">
              <svg class="w-5 h-5 mr-2 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
              列信息 ({{ columnCount }})
            </h4>
            
            <div v-if="hasColumns" class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200 border border-gray-200 rounded-lg">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">列名</th>
                    <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">数据类型</th>
                    <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">可空</th>
                    <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">默认值</th>
                    <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">备注</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="column in tableMetadata.columns" :key="column.columnName" class="hover:bg-gray-50">
                    <td class="px-4 py-3 text-sm font-medium text-gray-900">
                      {{ column.columnName }}
                      <span v-if="column.autoIncrement" class="ml-1 inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800">
                        自增
                      </span>
                    </td>
                    <td class="px-4 py-3 text-sm text-gray-700">
                      {{ column.dataType }}
                      <span v-if="column.columnSize" class="text-gray-500">({{ column.columnSize }})</span>
                    </td>
                    <td class="px-4 py-3 text-sm">
                      <span :class="column.nullable ? 'text-green-600' : 'text-red-600'">
                        {{ column.nullable ? '是' : '否' }}
                      </span>
                    </td>
                    <td class="px-4 py-3 text-sm text-gray-700">
                      {{ column.defaultValue || '-' }}
                    </td>
                    <td class="px-4 py-3 text-sm text-gray-700">
                      {{ column.remarks || '-' }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <div v-else class="text-center py-4 text-gray-500">
              暂无列信息
            </div>
          </div>

          <!-- Indexes (if available) -->
          <div v-if="hasIndexes">
            <h4 class="font-medium text-gray-900 mb-3 flex items-center">
              <svg class="w-5 h-5 mr-2 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
              索引信息
            </h4>
            <div class="space-y-2">
              <div v-for="index in tableMetadata.indexes" :key="index.indexName" class="bg-gray-50 rounded p-3">
                <div class="flex items-center justify-between">
                  <span class="font-medium text-gray-900">{{ index.indexName }}</span>
                  <span v-if="index.unique" class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-blue-100 text-blue-800">
                    唯一
                  </span>
                </div>
                <p class="text-sm text-gray-600 mt-1">
                  列: {{ index.columns?.join(', ') || '-' }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Error/Empty State -->
        <div v-else class="text-center py-8 text-gray-500">
          <p>无法加载表元数据</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TableMetadataModal',
  props: {
    tableMetadata: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['close'],
  computed: {
    columnCount() {
      return this.tableMetadata?.columns?.length || 0
    },
    hasColumns() {
      return this.columnCount > 0
    },
    hasIndexes() {
      return this.tableMetadata?.indexes?.length > 0
    }
  }
}
</script>