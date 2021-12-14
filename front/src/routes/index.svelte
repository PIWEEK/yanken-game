<script>
 import logo from "$lib/images/yanken.png" 
 import background from "$lib/images/select-bg.png"

 let name = "Kidd";
 
</script>

<div class="main" style="--background-image: url({background})">
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>
    <div class="inputs">
      <label for="name">Choose your name</label>
      <input name="name" type="text" bind:value={name} />
      <button>GO!</button>
    </div>
  </div>
</div>

<style lang="postcss">
 @import "$styles/forms.css";
 
 .main {
   width: 100%;
   height: 100%;
   background-image: var(--background-image);
   background-size: cover;
   padding: 30px;
 }

 .logo {
   width: 100%;
 }

 .inputs {
   display: flex;
   flex-direction: column;
 }
</style>
