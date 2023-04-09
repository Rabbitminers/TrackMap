<template>
  <main>
    <TrackMap/>
  </main>
</template>

<script lang="ts">
import TrackMap from '@/components/TrackMap.vue'

export default {
  components: {
      TrackMap
  }
}

</script>

<style lang="scss">
main {
  border-radius: 10px;
  background-color: var(--color-background-mute);
  margin: auto;
  max-width: 1024px;
  margin-top: 10vh;
  margin-bottom: 10vh;
}
</style>