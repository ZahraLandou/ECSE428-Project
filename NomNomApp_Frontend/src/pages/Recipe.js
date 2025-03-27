import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const Recipe = () => {
    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get('http://localhost:8081/recipes')
            .then(response => {
                if (Array.isArray(response.data)) {
                    setRecipes(response.data);
                } else {
                    console.error("Expected an array but got:", response.data);
                    setRecipes([]);
                }
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching recipes:", error);
                setLoading(false);
            });
    }, []);

    if (loading) return <p>Loading recipes...</p>;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">🍲 All Recipes</h1>
            {recipes.length === 0 ? (
                <p>No recipes found.</p>
            ) : (
                <ul className="space-y-4">
                    {recipes.map(recipe => (
                        <li key={recipe.recipeId} className="border p-4 rounded shadow">
                            <h2 className="text-xl font-semibold">{recipe.title}</h2>
                            <p className="text-gray-700">{recipe.description}</p>
                            <Link to={`/recipe/${recipe.recipeId}`} className="text-blue-600 hover:underline">
                                View Recipe →
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default Recipe;
