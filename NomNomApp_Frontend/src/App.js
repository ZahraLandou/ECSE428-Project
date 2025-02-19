import logo from './logo.svg';
import './App.css';
import CommentForm from './components/CommentForm';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          YAY FRONTEND IS SETUP
        </p>
        <a
          className="App-link"
          href="https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley"
          target="_blank"
          rel="noopener noreferrer"
        >
        hey group 1 click this for react help
        </a>
      </header>
      <div>
        <CommentForm />
      </div>
    </div>
  );
}


export default App;
