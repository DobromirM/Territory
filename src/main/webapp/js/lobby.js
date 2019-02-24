class Lobby extends React.Component
{
    static host = new URL(location.href).host;
    static webSocket = new WebSocket("ws://" + Lobby.host + "/lobby");
    static colors = ["pink", "aquamarine", "lawngreen", "khaki", "violet", "steelblue", "tomato"];
    static play = new URL(location.href).searchParams.get("play");
    
    componentDidMount()
    {
        
        Lobby.webSocket.onmessage = message => {
            
            let data = JSON.parse(message.data);
            this.setState({gameRooms: data.gameRoomSessions});
        };
    }
    
    constructor(props)
    {
        super(props);
        
        this.state = {
            gameRooms: null
        }
    }
    
    render()
    {
        if (Lobby.play)
        {
            return (<form action="game.html" method="get">
                <div className="lobby-title">Lobby</div>
                <div className="name">
                    <label>
                        Name:
                        <input type="text" name="name" required title="Letters only" pattern="^[a-zA-Z]{1,20}$"/>
                    </label>
                </div>
                Color:
                {Lobby.colors.map((color) => {
                    return <Color key={"color-" + color} color={color}/>
                })}
                {this.state.gameRooms && <GameRooms gameRooms={this.state.gameRooms} mode={"play"}/>}
                <Button mode={"play"}/>
            </form>)
        }
        else
        {
            return (<form action="game.html" method="get">
                <div className="lobby-title">Lobby</div>
                {this.state.gameRooms && <GameRooms gameRooms={this.state.gameRooms} mode={"watch"}/>}
                <input type="hidden" name="watch" value="true"/>
                <Button mode={"watch"}/>
            </form>)
        }
    }
}

class Color extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        
        return (<React.Fragment>
            <label htmlFor={this.props.color}> <span className="span-color" id={"color-" + this.props.color}/></label>
            <input type="radio"
                   id={this.props.color}
                   value={this.props.color}
                   name="color"
                   defaultChecked
                   required/>
        </React.Fragment>)
        
    }
}

class Button extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        if (this.props.mode === "play")
        {
            return (<div className="submit-button">
                <input type="submit" value="Play"/>
            </div>)
        }
        else
        {
            return (<div className="submit-button submit-button-watch">
                <input type="submit" value="Watch"/>
            </div>)
        }
    }
}

class GameRooms extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        if (this.props.mode === "play")
        {
            return (<div className={"rooms"}>Rooms:
                {this.props.gameRooms.filter((room) => {
                    return room.status === "WAITING_FOR_PLAYERS";
                }).map((room) => {
                    return <Room key={"room-" + room.id} room={room}/>
                })}
            </div>)
        }
        else
        {
            let rows = parseInt(this.props.gameRooms.length / 7) + 1;
            
            let htmlRows = [];
            for (let i = 0; i < rows; i++)
            {
                let row_index = i * 7;
                
                htmlRows.push(<div key={"rooms-" + i} className={"rooms"}>Rooms:
                    {this.props.gameRooms.slice(row_index, row_index + 7).map((room) => {
                        return <Room key={"room-" + i + "-" + room.id} room={room}/>
                    })}
                </div>)
            }
            
            return (<div>{htmlRows}</div>)
        }
    }
}

class Room extends React.Component
{
    constructor(props)
    {
        super(props);
    }
    
    render()
    {
        let status = "Waiting for players";
        let players = "Players: " + this.props.room.players.length + "/5";
        
        if (this.props.room.status === "PLAYING")
        {
            status = "Playing"
        }
        
        if (this.props.room.status === "FINISHED")
        {
            status = "Finished!";
            players = null;
        }
        
        return (<React.Fragment>
            <label htmlFor={"room-" + this.props.room.id}>
                <span className={"room"}></span>
            </label>
            <span className="room-id">Room ID: {this.props.room.id}</span>
            <span className="room-status"><span className="room-status-text">{status}</span></span>
            <span className="room-players">{players}</span>
            <input type="radio" id={"room-" + this.props.room.id} value={this.props.room.id} name="id" required
                   defaultChecked/>
        
        </React.Fragment>)
    }
}

ReactDOM.render(<Lobby/>, document.getElementById('main'));
