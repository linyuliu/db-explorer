<template>
  <div class="bg-white p-6 rounded-2xl shadow-lg border border-gray-200 h-fit">
    <h2 class="text-xl font-semibold mb-6 border-b pb-3 text-gray-800 flex items-center">
      <svg class="w-6 h-6 mr-2 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 9l3 3-3 3m5 0h3M5 20h14a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
      连接配置
    </h2>
    
    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- Database Type -->
      <div>
        <label for="db-type" class="block text-sm font-medium text-gray-700 mb-1">
          数据库类型
        </label>
        <select 
          v-model="form.databaseType" 
          @change="onDbTypeChange" 
          id="db-type" 
          class="form-select mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition"
        >
          <option v-for="db in dbTypes" :key="db.value" :value="db.value">
            {{ db.name }}
          </option>
        </select>
      </div>

      <!-- Host and Port -->
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label for="host" class="block text-sm font-medium text-gray-700 mb-1">
            主机
          </label>
          <input 
            v-model="form.host" 
            type="text" 
            id="host" 
            class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition" 
            placeholder="localhost"
          >
        </div>
        <div>
          <label for="port" class="block text-sm font-medium text-gray-700 mb-1">
            端口
          </label>
          <input 
            v-model.number="form.port" 
            type="number" 
            id="port" 
            class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition"
          >
        </div>
      </div>

      <!-- Database -->
      <div>
        <label for="database" class="block text-sm font-medium text-gray-700 mb-1">
          {{ dbNameLabel }}
        </label>
        <input 
          v-model="form.database" 
          type="text" 
          id="database" 
          class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition"
        >
      </div>

      <!-- Schema (conditional) -->
      <div v-if="selectedDbType.supportsSchema">
        <label for="schema" class="block text-sm font-medium text-gray-700 mb-1">
          模式 (Schema)
        </label>
        <input 
          v-model="form.schema" 
          type="text" 
          id="schema" 
          class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition" 
          :placeholder="selectedDbType.schemaHint"
        >
      </div>

      <!-- Username and Password -->
      <div class="grid grid-cols-2 gap-4">
        <div>
          <label for="username" class="block text-sm font-medium text-gray-700 mb-1">
            用户名
          </label>
          <input 
            v-model="form.username" 
            type="text" 
            id="username" 
            class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition" 
            placeholder="root"
          >
        </div>
        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">
            密码
          </label>
          <input 
            v-model="form.password" 
            type="password" 
            id="password" 
            class="mt-1 block w-full rounded-lg border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 transition" 
            placeholder="******"
          >
        </div>
      </div>

      <!-- Submit Button -->
      <button 
        type="submit" 
        :disabled="disabled" 
        class="w-full mt-6 bg-blue-600 text-white font-semibold py-3 px-4 rounded-lg shadow-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75 transition duration-150 ease-in-out disabled:bg-blue-400 disabled:cursor-not-allowed flex items-center justify-center"
      >
        <svg 
          v-if="disabled" 
          class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" 
          xmlns="http://www.w3.org/2000/svg" 
          fill="none" 
          viewBox="0 0 24 24"
        >
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <span>{{ disabled ? '正在连接...' : '连接并获取表结构' }}</span>
      </button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'ConnectionPanel',
  props: {
    disabled: {
      type: Boolean,
      default: false
    }
  },
  emits: ['connect', 'loading'],
  data() {
    return {
      dbTypes: [
        { value: 'mysql8', name: 'MySQL 8.x+', port: 3306, dbLabel: '数据库', dbDefault: 'mysql', supportsSchema: false },
        { value: 'mysql5', name: 'MySQL 5.x', port: 3306, dbLabel: '数据库', dbDefault: 'mysql', supportsSchema: false },
        { value: 'postgresql', name: 'PostgreSQL', port: 5432, dbLabel: '数据库', dbDefault: 'postgres', supportsSchema: true, schemaHint: '例如: public' },
        { value: 'oracle', name: 'Oracle', port: 1521, dbLabel: '服务名 / SID', dbDefault: 'ORCL', supportsSchema: true, schemaHint: '通常为用户名大写' },
        { value: 'sqlserver', name: 'SQL Server', port: 1433, dbLabel: '数据库', dbDefault: 'master', supportsSchema: true, schemaHint: '例如: dbo' },
        { value: 'dameng', name: '达梦 (Dameng)', port: 5236, dbLabel: '数据库', dbDefault: 'SYSTEM', supportsSchema: true, schemaHint: '例如: SYS' },
        { value: 'kingbase', name: '人大金仓 (KingbaseES)', port: 54321, dbLabel: '数据库', dbDefault: 'postgres', supportsSchema: true, schemaHint: '例如: public' }
      ],
      form: {
        databaseType: 'mysql8',
        host: 'localhost',
        port: 3306,
        database: 'mysql',
        schema: '',
        username: 'root',
        password: ''
      }
    }
  },
  computed: {
    selectedDbType() {
      return this.dbTypes.find(db => db.value === this.form.databaseType) || {}
    },
    dbNameLabel() {
      return this.selectedDbType.dbLabel || '数据库'
    }
  },
  methods: {
    onDbTypeChange() {
      const db = this.selectedDbType
      if (db) {
        this.form.port = db.port
        this.form.database = db.dbDefault
        this.form.schema = '' // Reset schema
      }
    },
    handleSubmit() {
      this.$emit('connect', { ...this.form })
    }
  },
  mounted() {
    this.onDbTypeChange()
  }
}
</script>

<style scoped>
.form-select {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
  padding-right: 2.5rem;
  appearance: none;
}
</style>