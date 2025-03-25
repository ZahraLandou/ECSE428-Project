import logo from './logo.svg';
import './App.css';
import CommentForm from './components/CommentForm';

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Recipe from './pages/Recipe';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={
                    <div className="App">
                        <header className="App-header">
                            <p>🍽️ YAY FRONTEND IS SETUP</p>
                            <a
                                className="App-link"
                                href="https://www.youtube.com/watch?v=dQw4w9WgXcQ"
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
                } />

                {}
                <Route path="/recipe/:recipeId" element={<Recipe />} />
            </Routes>
        </Router>
    );
}

export default App;
