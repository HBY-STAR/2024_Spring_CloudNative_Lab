<script setup lang="ts">
  import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <RouterView v-slot="slotProps">
    <keep-alive>
      <component :is="slotProps.Component"></component>
    </keep-alive>
  </RouterView>
</template>

<style scoped></style>
