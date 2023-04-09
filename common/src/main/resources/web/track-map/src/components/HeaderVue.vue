<template>
    <header class="site-header" role="presentation">    
        <Logo class="logo" size="l"/>
        <h1 class="header-title">Create: <br>Track Map</h1>
        <nav>
            <a class="navigation-path" href="/about">About</a>
            <a class="navigation-path" href="/api">Api</a>
        </nav>
    </header>
</template>

<script lang="ts">
import Logo from './icons/Logo.vue';

export default {
    components: {
        Logo
    },
    methods: {
        toggleDarkMode() {
            const prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            const body = document.body;
            if (prefersDark) {
                body.classList.add('dark');
            }
            body.classList.toggle('dark');
        }
    }
}
</script>

<style lang="scss">
header {
    background-color: var(--color-background);
    width: 100%;
    height: 8em;
    padding: 10px;
    gap: 1em;
    display: flex;
    align-items: center;    

    > .logo {
        height: 100%;
    }
}

.header-title {
    transition: all 0.7s;
    line-height: normal;
    flex-grow: 1;

    &:hover {
        color: var(--colour-primary);
    }
}

nav {
    display: grid;
    grid-template-columns: 4em 4em;
}

@media only screen and (max-width: 12in) {
    nav {
        display: none;
    }
}

.navigation-path {
    transition: all 0.7s;
    color: var(--color-text);

    &:hover {
        color: var(--colour-primary);
    }

    &:active {
        text-decoration: underline;
    }
}

.logo {
    border-style: solid;
    border-radius: 100%;
    border-color: var(--color-border);
    border-style: none;
    transition: all 1s;

    &:hover {
        border-color: var(--color-border-hover);
        border-style: solid;
        border-width: 10px;
    }
}

</style>