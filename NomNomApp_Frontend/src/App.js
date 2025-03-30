import logo from './logo.svg';
import './App.css';
import CommentForm from './components/CommentForm';
import RecipeRating from './pages/RecipeRating';
import Recipe from './pages/Recipe';
import ViewCollections from './pages/ViewCollections';

import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link} from 'react-router-dom';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={
                    <div className="App">
                        <header className="App-header">
                            <p>🍽️ NomNom 🍽️</p>
                        </header>
                        <div>
                            <CommentForm />
                        {/*Collections button*/}
                        <div>
                            <Link to="/view-collections">
                                <button>View Collections</button>
                            </Link>
                        </div>
                        </div>
                    </div>
                } />

                {}
                <Route path="/recipe" element={<Recipe />} />
                <Route path="/recipe/:recipeId/rate" element={<RecipeRating />} />
                <Route path="/view-collections" element={<ViewCollections />} />
            </Routes>
        </Router>
    );
}

export default App;
