<template>
<footer>
    <div class="footer-column">
        <h3 class="footer-header">About</h3>
        

        Create: Track Map is open source you can view and contribute to this project <a href="https://github.com/rabbitminers/TrackMap">here</a> on github
    </div>
    <div class="footer-column">
        <h3 class="footer-header">Found A Bug?</h3>
        You can report any bugs you find on github or <a href="https://discord.gg/GeGJmZ5v">discord</a>

    </div>
    <div class="footer-column">
        <h3 class="footer-header">Contact Us</h3>
        Want to suggest a feature, ask a question or recieve progress updates on development you can find us on GitHub here and Discord <a href="https://discord.gg/GeGJmZ5v">here</a>
    </div>
</footer>
</template>

<script lang="ts">
export default {

}
</script>

<style lang="scss">

footer {
    background-color: var(--color-background);
    display: flex;
    flex-direction: column;
    height: fit-content;
    min-height: 50vh;

    >.footer-column {
        text-align: center;
        margin: 20px;
    }
}

@media only screen and (min-width: 12in) {
    footer {
        background-color: var(--color-background);
        width: 100%;
        display: grid;
        grid-template-columns: 33% 33% 33%;
    
        >.footer-column {
            text-align: left;
        }
    }   
}

.footer-header {
    color: var(--colour-primary);
}
</style>