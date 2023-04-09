<template>
    <HeaderVue/>
    <HomeView/>
    <FooterVue/>
</template>

<script lang="ts">
import HomeView from './views/HomeView.vue';
import HeaderVue from './components/HeaderVue.vue';
import FooterVue from './components/FooterVue.vue';

export default {
  components: {
    HomeView,
    HeaderVue,
    FooterVue
  }
}
</script>

<style lang="scss">
</style>