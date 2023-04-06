import { TrackMap } from '../data/Map';
<template>
    <canvas id="track-map" ref="canvas"></canvas>
</template>

<script lang="ts">
import TrackMap from '@/data/Map'

export default {
    name: "Track Map",
    mounted() {
        const canvas = this.$refs.canvas as HTMLCanvasElement;
        const trackMap = new TrackMap(canvas);
        trackMap.init();
    }
}
</script>

<style>
canvas {
    width: 100%;
    height: 100%;
}
</style>