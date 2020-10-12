## Player to Server

```ts
questionAnswered => answer: string 

playerDisconnect => void  

readyToPlay => void  
```


## Host Player to Server


```ts
startGame => void  

kickMember => memberName: string
```


## Server to Player


```ts
gameFinished => winners: { Name: string; Points: number }[]  

roundOver => solution: string, playerPoints: number, topPlayers: { Name: string; Points: number }[]  

playersChange => players: string[]  max:number

newQuestion => question: { question: string; prompt: string }  

gameStart => void  

answerResult => correct: boolean
```