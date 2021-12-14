<script context="module" lang="ts">
 // import { goto } from "$app/navigation";

 import rock from "$lib/images/rock.png";
 import paper from "$lib/images/paper.png";
 import scissors from "$lib/images/scissors.png";
 import background from "$lib/images/play-bg.png";
 import handsBg from "$lib/images/hands-bg.png";
 import selectBg from "$lib/images/select.png";
 import clockIcon from "$lib/images/timer.svg";

 import MatchData from "$components/MatchData.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
</script>

<div class="game"
     style="--background-image: url({background}); 
            --background-hands: url({handsBg});
            --background-select: url({selectBg});
            --clock-icon: url({clockIcon})">
  <div class="turn-info">
    <div class="turn-display">Round 2</div>
    <div class="clock" class:low={true}>
      <span>99</span>
    </div>
  </div>

  <div class="players-info">
    <MatchData/>
    <MatchData/>
    <MatchData/>
    <MatchData/>
    <MatchData/>
    <MatchData/>
    <MatchData/>
  </div>

  <div class="play-area">
    <div class="message">You Lose!</div>
    <div class="player-left">
      <PlayerCard name="Nombrelargodelcarajo"
                  color="green"
                  cardType="full"
                  result="win"
                  flipx={true}/>
    </div>

    <div class="player-right">
      <PlayerCard name="Cletus"
                  color="red"
                  result="loss"
                  cardType="full"/>
    </div>
  </div>

  <div class="actions">
    <button class="action rock" class:selected={true}>
      <img alt="Rock" src={rock}/>
    </button>
    <button class="action paper" class:selected={true}>
      <img alt="Paper"src={paper}/>
    </button>
    <button class="action scissors" class:selected={true}>
      <img alt="Scissors" src={scissors}/>
    </button>
  </div>
</div>

<style lang="postcss">
 .game {
   background-image: var(--background-image);
   background-size: cover;
   flex: 1;
   height: 100%;
   width: 100%;

   &::before {
     background-image: var(--background-hands);
     background-size: cover;
     bottom: 0;
     content: '';
     height: 100%;
     pointer-events: none;
     position: absolute;
     width: 100%;
     opacity: 0.5;
   }

   & > * {
     z-index: 1;
   }
 }

 .game {
   display: grid;
   grid-template-areas:
     "turn-info"
     "players-info"
     "play-area"
     "actions";

   grid-template-columns: initial;
   grid-template-rows: auto 170px 1fr auto;
   grid-template-columns: 100%;

   & .turn-info {
     grid-area: turn-info;
   }

   & .players-info {
     grid-area: players-info;
   }

   & .play-area {
     grid-area: play-area;
   }

   & .actions {
     grid-area: actions;
   }
 }

 .turn-info {
   padding: 1rem;
   display: flex;
   flex-direction: row;
   justify-content: space-between;

   & .turn-display {
     padding: 4px 8px;
     background-color: #222034;
   }

   & .clock {
     width: 50px;
     padding: 4px 8px;
     text-align: right;
     background-color: #222034;
     position: relative;

     &.low {
       background-color: #e03333;
     }

     &::before {
       content: '';
       width: 10px;
       height: 15px;
       position: absolute;
       left: 10px;
       top: 5px;
       background-image: var(--clock-icon);
       background-size: cover;
     }
   }
 }

 .players-info {
   display: flex;
   flex-direction: column;
   flex-wrap: wrap;
   margin: 0 1rem;
   overflow-x: scroll;

   & > :global(*) {
     width: 120px;
     margin-top: 0.5rem;
     margin-right: 1.5rem;
   }
 }

 .play-area {
   display: grid;

   grid-template-areas: "message message" "player-left player-right";
   grid-template-columns: 1fr 1fr;
   grid-template-rows: auto 1fr;
   padding: 0 16px;

   & .message {
     grid-area: message;
     text-align: center;
     text-shadow: 8px 8px rgba(0,0,0,40%);
     font-size: 32px;
   }

   & .player-left {
     grid-area: player-left;
   }

   & .player-right {
     grid-area: player-right;
   }

   & .player-right, & .player-left {
     display: flex;
     align-items: center;
     justify-content: center;
   }
 }
 
 .actions {
   grid-area: buttons;
   border-top: 2px dashed #8e489c;
   padding-top: 1rem;
   margin: 0 1.5rem 0.5rem 1.5rem;
   display: grid;
   grid-template-columns: repeat(3, 70px);
   grid-template-rows: 114px;
   grid-gap: 50px;

   & .action {
     padding: 0;
     border: none;
     background: none;
     position: relative;

     &.selected::before {
       content: '';
       width: 122px;
       height: 122px;
       position: absolute;
       left: -30px;
       top: -6px;
       z-index: -1;
       background-image: var(--background-select);
       background-size: cover;
     }

     & img {
       width: 100%;
     }
   }
 }
 
</style>
