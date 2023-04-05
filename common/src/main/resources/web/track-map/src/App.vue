<template>
  <HeaderVue/>
  <HomeView/>
</template>

<script>
import HomeView from './views/HomeView.vue';
import HeaderVue from './components/HeaderVue.vue';

export default {
  components: {
    HomeView,
    HeaderVue
  }
}
</script>