<script lang="ts">
 import sucBlack from "$lib/images/sucubus-black.png";
 import sucBlue from "$lib/images/sucubus-blue.png";
 import sucGreen from "$lib/images/sucubus-green.png";
 import sucOrange from "$lib/images/sucubus-orange.png";
 import sucPink from "$lib/images/sucubus-pink.png";
 import sucPurple from "$lib/images/sucubus-purple.png";
 import sucRed from "$lib/images/sucubus-red.png";
 import sucWhite from "$lib/images/sucubus-white.png";
 import sucYellow from "$lib/images/sucubus-yellow.png";

 import selectBg from "$lib/images/gr-bg.png";
 import defeat from "$lib/images/death.png"
 import victory from "$lib/images/victory.png"

 import rock from "$lib/images/rock.png";
 import paper from "$lib/images/paper.png";
 import scissors from "$lib/images/scissors.png";

 import sucRock from "$lib/images/sucubus-rock.png";
 import sucPaper from "$lib/images/sucubus-paper.png";
 import sucScissors from "$lib/images/sucubus-scissors.png";
 import sucBot from "$lib/images/sucubus-bot.png";


 type AvatarColor =
   "black" | "blue" | "green" | "orange" |
   "pink" | "purple" | "red" | "white" | "yellow";

 export const AVATARS: Record<AvatarColor, string> = {
   black: sucBlack,
   blue: sucBlue,
   green: sucGreen,
   orange: sucOrange,
   pink: sucPink,
   purple: sucPurple,
   red: sucRed,
   white: sucWhite,
   yellow: sucYellow,
 }

 type Hand = "rock" | "paper" | "scissors";
 export const HANDS: Record<Hand, string> = {
   rock: sucRock,
   paper: sucPaper,
   scissors: sucScissors
 }

 export let name: string | null = null;
 export let avatar: string = "red-stone";
 export let cardType: "full" | "small" | null = null;
 export let flipx: boolean = false;

 export let pick: "rock" | "paper" | "scissors" | null = null;
 export let result: "win" | "loss" | "draw" | null = null;
 export let lastPlays: string[];

 let color: AvatarColor = "red";
 let hand: Hand = "rock";

 $: color = (avatar.split("-")[0] as AvatarColor);
 $: hand = (avatar.split("-")[1] as Hand);

 $: nameValue = name ? ([...name].reduce((a,b) => a + b.charCodeAt(0), 0)) % Object.values(AVATARS).length : 0;

</script>

<div class="suc-data"
     class:flipx={flipx}
     class:small={cardType === "small"}
     class:full={cardType === "full"}
     class:dimmed={result === "loss"}
     style="--select-bg: url({selectBg})">
  <img class="avatar" alt={color} src={AVATARS[color] || Object.values(AVATARS)[nameValue]}/>
  <img class="hand" alt={hand} src={HANDS[hand] || sucBot}/>

  {#if name}
    <div class="player-name">{name}</div>
  {/if}

  {#if cardType === "full" && lastPlays}
    <div class="last-plays-title">Last Plays</div>
    <div class="last-plays">
      {#each lastPlays as pick}
        <div class="last-play-item">
          {#if pick === "rock"}
            <img src={rock} alt="Rock" />
          {:else if pick === "paper"}
            <img src={paper} alt="Paper" />
          {:else if pick === "scissors"}
            <img src={scissors} alt="Scissors" />
          {/if}
        </div>
      {/each}
    </div>
  {/if}
  <div class="decoration">
    {#if result === "win"}
      <img class="turn-result" src={victory} alt="Victory" />
    {:else if result === "loss"}
      <img class="turn-result" src={defeat} alt="Defeat" />
    {/if}

    {#if pick}
      <div class="turn-pick">
        {#if pick === "rock"}
          <img src={rock} alt="Rock" />
        {:else if pick === "paper"}
          <img src={paper} alt="Paper" />
        {:else if pick === "scissors"}
          <img src={scissors} alt="Scissors" />
        {/if}
      </div>
    {/if}
  </div>
</div>

<style lang="postcss">
 .suc-data {
   position: relative;

   & .player-name {
     overflow: hidden;
     text-overflow: ellipsis;
     text-align: center;
     white-space: nowrap
   }

   & .avatar, & .hand {
     width: 100%;
     border: 2px solid #8e489c;
   }

   & .hand {
     position: absolute;
     top: 0;
     left: 0;
     z-index: 100;
   }

   &.dimmed .avatar, &.dimmed .hand  {
     opacity: 50%;
   }

   &.flipx .avatar, &.flipx .hand {
     transform: scale(-1, 1);
   }

   &.full {
     width: 108px;
   }

   &.small {
     width: 50px;

     & .player-name {
       width: 50px;
       font-size: 10px;
     }
   }
 }

 .turn-result {
   position: absolute;
   width: 40px;
   height: 40px;
   top: -20px;
   right: -20px;
   filter: drop-shadow(6px 6px 0px rgba(0, 0, 0, 40%));
 }

 .suc-data:not(.small) .turn-result {
   top: 0;
   left: 0;
   right: initial;
 }

 .turn-pick {
   align-items: center;
   background-image: var(--select-bg);
   background-size: cover;
   bottom: 40px;
   display: flex;
   flex-direction: row;
   height: 90px;
   justify-content: center;
   position: absolute;
   right: -40px;
   width: 90px;

   & img {
     width: 50px;
   }
 }

 .flipx .turn-result {
   right: initial;
   left: -20px;
 }

 .flipx .turn-pick {
   right: initial;
   left: -40px;
 }

 .small .turn-result {
   display: none;
 }

 .small .turn-pick {
   display: none;
 }

 .last-plays-title {
   font-size: 12px;
   text-align: center;
   color: #de8eee;
 }

 .last-plays {
   display: flex;
   flex-direction: row;
   padding: 3px;
   margin: 4px;
   justify-content: center;
   height: 30px;

   & .last-play-item {
     height: 25px;
     width: 25px;
     display: flex;
     flex-direction: column;
     justify-content: center;
     padding: 2px;

     & img {
       width: 100%;
     }
   }
 }

 @media only screen and (min-width: 900px) {
   .suc-data {

     & .avatar {
       border: 4px solid #8e489c;
     }

    &.small {
      width: 120px;

      & .player-name {
        font-size:16px;
        width: 100%;
      }

      & .avatar {
        border: 4px solid #8e489c;
      }
    }

    &.full {
      width: 280px;

      & .player-name {
        font-size:26px;
      }

      & .last-plays-title {
        font-size: 16px;
      }

      & .last-play-item {
        width: 50px;
        height: 50px;
        padding: 8px;
      }

      & .avatar {
        border: 8px solid #8e489c;
      }
    }
   }
   .turn-result {
     width: 100px;
     height: 100px;
     top: -50px;
     right: -50px;
   }

   .suc-data:not(.small) .turn-result {
     top: 33px;
     left: 66px;
     right: initial;
   }

   .flipx .turn-result {
     right: initial;
     left: -50px;
   }
   .flipx .turn-pick {
     right: initial;
     left: -50px;
   }
 }
</style>
