<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <DatabaseExplorer />
  </div>
</template>

<script>
import DatabaseExplorer from './components/DatabaseExplorer.vue'

export default {
  name: 'App',
  components: {
    DatabaseExplorer
  }
}
</script>

<style>
/* Custom scrollbar styles */
.custom-scrollbar::-webkit-scrollbar {
  width: 8px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #94a3b8;
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #64748b;
}
</style>
