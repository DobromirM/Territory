class GameSession extends React.Component
{
    static host = new URL(location.href).host;
    static watch = new URL(location.href).searchParams.get("watch");
    static sessionId = new URL(location.href).searchParams.get("id");
    static playerName = new URL(location.href).searchParams.get("name");
    static playerColor = new URL(location.href).searchParams.get("color");
    static webSocket = null;
    static selectedTile = null;
    static selectedCard = null;
    static yourId = null;
    static gameStatus = "WAITING_FOR_PLAYERS";
    
    componentDidMount()
    {
        GameSession.webSocket.onmessage = message => {
            
            let data = JSON.parse(message.data);
            
            if (data.you)
            {
                GameSession.yourId = data.you;
            }
            
            GameSession.gameStatus = data.session.status;
            this.setState({gameState: data.session, errorMessage: data.errorMessage});
        };
        
        GameSession.webSocket.onclose = message => {
            this.setState({closed: true});
        };
    }
    
    constructor(props)
    {
        super(props);
        
        if (GameSession.watch)
        {
            GameSession.webSocket = new WebSocket("ws://" + GameSession.host + "/watch/" + GameSession.sessionId);
        }
        else
        {
            GameSession.webSocket = new WebSocket("ws://" + GameSession.host + "/play/" + GameSession.sessionId + "/" + GameSession.playerName + "/" + GameSession.playerColor);
        }
        
        this.state = {
            gameState: null, errorMessage: null, closed: null
        }
    }
    
    render()
    {
        if (this.state.gameState)
        {
            
            let status = "Waiting for players";
            let winner = null;
            let playerOnTurn = null;
            
            if (this.state.gameState.status === "PLAYING")
            {
                status = "Playing";
                playerOnTurn = "Player on turn: " + this.state.gameState.playerOnTurn.name + " id:" + this.state.gameState.playerOnTurn.id;
            }
            
            if (this.state.gameState.status === "FINISHED")
            {
                status = "Finished!";
                
                if (this.state.gameState.winner)
                {
                    winner = " Winner - " + this.state.gameState.winner.name + "(ID: " + this.state.gameState.winner.id + ")";
                }
                else
                {
                    winner = "No winner";
                }
                
                playerOnTurn = null;
            }
            
            return (<div className="game">
                <div className="game-status">{status} <span className="game-winner">{winner}</span></div>
                <div className="player-on-turn">{playerOnTurn}</div>
                <div className="game-error">{this.state.errorMessage}</div>
                <div className="game-board">
                    <Board board={this.state.gameState.board} players={this.state.gameState.players}/>
                </div>
                <div className="players-area">
                    <PlayersArea players={this.state.gameState.players}/>
                </div>
            </div>);
        }
        else
        {
            if (this.state.closed)
            {
                return (<div className="disconnected">
                    <div className="disconnected-text">Cannot connect to this room right now.</div>
                </div>)
            }
            else
            {
                return null;
            }
        }
    }
}

class Board extends React.Component
{
    getTileValue(x, y)
    {
        return this.props.board.tiles.find(function (element) {
            return element.coordinates.x === x && element.coordinates.y === y;
        }).value;
    }
    
    getTileColor(tileValue)
    {
        if (tileValue)
        {
            let player = this.props.players.find(function (element) {
                return element.id === tileValue;
            });
            
            if (player)
            {
                return player.color;
            }
            else
            {
                return "grey";
            }
        }
        else
        {
            return "";
        }
    }
    
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        return (<div className="board">
            {Array(this.props.board.rows).fill().map((_, index) => index).map((row) => {
                return <div key={"row-" + row} className="board-row">
                    {Array(this.props.board.columns).fill().map((_, index) => index).map((col) => {
                        
                        let tileValue = this.getTileValue(row, col);
                        let tileColor = this.getTileColor(tileValue);
                        
                        return <Tile key={"col-" + col} x={row} y={col} color={tileColor} value={tileValue}/>
                    })}
                </div>
                
            })}
        </div>)
    }
}

class Tile extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            selected: "unselected"
        }
    }
    
    static sendMove(tile)
    {
        let cardValue = 'NONE';
        
        if (GameSession.selectedCard)
        {
            cardValue = GameSession.selectedCard.props.card;
        }
        
        if (cardValue === 'DOUBLE')
        {
            if (GameSession.selectedTile)
            {
                let move = {
                    card: cardValue,
                    firstCoord: {x: GameSession.selectedTile.props.x, y: GameSession.selectedTile.props.y},
                    secondCoord: {x: tile.props.x, y: tile.props.y}
                };
                GameSession.webSocket.send(JSON.stringify({move: move}));
                GameSession.selectedTile.setState({selected: "unselected"});
                GameSession.selectedTile = null;
                GameSession.selectedCard.setState({selected: "not-selected"});
                GameSession.selectedCard = null;
                
            }
            else
            {
                GameSession.selectedTile = tile;
                GameSession.selectedTile.setState({selected: "selected"})
            }
            
        }
        else
        {
            let move = {
                card: cardValue, firstCoord: {x: tile.props.x, y: tile.props.y}, secondCoord: {x: null, y: null}
            };
            
            GameSession.webSocket.send(JSON.stringify({move: move}));
            
            if (GameSession.selectedCard)
            {
                GameSession.selectedCard.setState({selected: "not-selected"});
                GameSession.selectedCard = null;
            }
            
        }
    }
    
    render()
    {
        return (<button className={"tile" + " " + this.state.selected} style={{backgroundColor: this.props.color}}
                        onClick={() => Tile.sendMove(this)}>
            {this.props.value}
        </button>);
    }
}

class PlayersArea extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        return (<div className={"area"}>
            {this.props.players.map((player) => {
                return <Player you={player.id === GameSession.yourId} key={"player-" + player.id} player={player}/>
            })}
        </div>)
    }
}

class Player extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        let playerClass = ["player"];
        if (this.props.you)
        {
            playerClass.push("you");
        }
        
        if (GameSession.gameStatus === "WAITING_FOR_PLAYERS")
        {
            return (<div className={playerClass.join(' ')} style={{backgroundColor: this.props.player.color}}
                         id={this.props.player.id}>{this.props.player.name} ID: {this.props.player.id}
                <Hand key={"hand-" + this.props.player.id} cards={this.props.player.cards}/>
                <ReadyButton key={"ready-" + this.props.player.id} ready={this.props.player.ready}/>
            </div>)
        }
        else
        {
            return (<div className={playerClass.join(' ')} style={{backgroundColor: this.props.player.color}}
                         id={this.props.player.id}>{this.props.player.name} id:{this.props.player.id}<br/>Points: {this.props.player.points}
                <Hand key={"hand-" + this.props.player.id} cards={this.props.player.cards}/>
            </div>)
        }
    }
}

class Hand extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        return (<div className="hand">
            {this.props.cards.map((card) => {
                return <Card key={card} card={card}/>
            })}
        </div>)
    }
}

class Card extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            selected: "not-selected"
        }
    }
    
    toggleCard()
    {
        if (this.state.selected === "not-selected")
        {
            if (GameSession.selectedCard)
            {
                GameSession.selectedCard.setState({selected: "not-selected"});
            }
            
            this.setState({selected: "selected"});
            GameSession.selectedCard = this;
        }
        else
        {
            if (this.props.card === "DOUBLE")
            {
                if (GameSession.selectedTile)
                {
                    GameSession.selectedTile.setState({selected: "unselected"});
                    GameSession.selectedTile = null;
                }
            }
            
            this.setState({selected: "not-selected"});
            GameSession.selectedCard = null;
        }
    }
    
    render()
    {
        return (<div className={"card" + " " + this.state.selected} id={this.props.card}
                     onClick={() => this.toggleCard()}>{this.props.card}</div>)
    }
}

class ReadyButton extends React.Component
{
    toggleReady()
    {
        GameSession.webSocket.send(JSON.stringify({ready: !this.props.ready}));
    }
    
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        let readyClass = ["ready"];
        if (this.props.ready)
        {
            readyClass.push("active")
        }
        
        return (<button className={readyClass.join(' ')} id={this.props.card}
                        onClick={() => this.toggleReady()}>Ready</button>)
    }
}

ReactDOM.render(<GameSession/>, document.getElementById('main'));