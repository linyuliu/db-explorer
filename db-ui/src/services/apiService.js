import axios from 'axios'

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 30000, // 30 seconds
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor
apiClient.interceptors.request.use(
  config => {
    console.log('API Request:', config.method?.toUpperCase(), config.url)
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
apiClient.interceptors.response.use(
  response => {
    console.log('API Response:', response.status, response.config.url)
    return response.data
  },
  error => {
    console.error('Response error:', error)
    
    // Handle different error scenarios
    if (error.response) {
      // Server responded with error status
      const errorMessage = error.response.data?.error || error.response.statusText || 'Server error'
      throw new Error(`[${error.response.status}] ${errorMessage}`)
    } else if (error.request) {
      // Request was made but no response received
      throw new Error('无法连接到后端服务。请检查后端服务是否已在 8080 端口启动，并确认网络连接没有问题。')
    } else {
      // Something happened in setting up the request
      throw new Error('请求发送失败: ' + error.message)
    }
  }
)

const apiService = {
  /**
   * Connect to database and get table list
   */
  async connect(connectionRequest) {
    return await apiClient.post('/connect', connectionRequest)
  },

  /**
   * Get detailed metadata for a specific table
   */
  async getTableMetadata(connectionRequest, tableName) {
    return await apiClient.post(`/table-metadata?tableName=${encodeURIComponent(tableName)}`, connectionRequest)
  },

  /**
   * Get list of schemas for databases that support them
   */
  async getSchemas(connectionRequest) {
    return await apiClient.post('/schemas', connectionRequest)
  }
}

export default apiService