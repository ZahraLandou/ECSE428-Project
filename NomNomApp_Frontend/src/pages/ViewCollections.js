import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ViewCollections = () => {
    // Simulate logged in user with userId 0
    const userId = 0; //TODO change this eventually to a fetch from the login page

    const [collections, setCollections] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [newCollectionName, setNewCollectionName] = useState("");
    const [selectedCollection, setSelectedCollection] = useState(null);
    const [showRecipeSelector, setShowRecipeSelector] = useState(false);

    // Fetch collections and recipes on component mount
    useEffect(() => {
        const fetchData = async () => {
            try {
                const collectionsRes = await axios.get(`http://localhost:3000/api/collections/user/${userId}`);
                setCollections(collectionsRes.data);
                const recipesRes = await axios.get(`http://localhost:3000/api/recipes`);
                setRecipes(recipesRes.data);
            } catch (err) {
                console.error("Error fetching data:", err);
            }
        };
        fetchData();
    }, [userId]);

    // Create a new collection
    const handleAddCollection = async () => {
        if (!newCollectionName.trim()) {
            alert("ERROR: Name can't be empty");
            return;
        }

        try {
            // POST to backend to create collection
            await axios.post(`http://localhost:3000/api/collections`, {
                userId,
                name: newCollectionName,
            });

            // Refresh
            const res = await axios.get(`http://localhost:3000/api/collections/user/${userId}`);
            setCollections(res.data);
            setNewCollectionName("");
            setShowForm(false);
        } catch (err) {
            console.error("Error adding collection:", err);
            alert(err);
        }
    };

    // Add recipe to collection
    const handleAddRecipeToCollection = async (recipeId) => {
        try {
            await axios.post(`http://localhost:3000/api/collections/${selectedCollection}/recipes`, {
                recipeId
            });
            alert("Recipe added to collection successfully!");
            setShowRecipeSelector(false);
        } catch (err) {
            console.error("Error adding recipe to collection:", err);
            alert("Failed to add recipe to collection.");
        }
    };

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h1 className="text-3xl font-bold mb-2">📂 Recipe Collections</h1>

            {/* List of Collections */}
            {collections.length > 0 ? (
                <ul className="list-none space-y-2">
                    {collections.map((collection) => (
                        <li key={collection.id} className="flex justify-between items-center py-2 border-b">
                            <span>{collection.name}</span>
                            <div>
                                <button
                                    onClick={() => {
                                        setSelectedCollection(collection.id);
                                        setShowRecipeSelector(true);
                                    }}
                                    className="ml-2 bg-green-500 text-white px-2 py-1 rounded hover:bg-green-600 text-sm"
                                    title="Add recipe to this collection"
                                >
                                    ➕ Add Recipe
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="text-gray-500">You have not created any collections yet</p>
            )}

            {/* Add Collection Button */}
            <button
                onClick={() => {
                    setShowForm(!showForm);
                    if (showForm) {
                        setNewCollectionName("");
                    }
                }}
                className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
                {showForm ? "Cancel" : "➕ Add Collection"}
            </button>

            {/* Form for creating a new collection */}
            {showForm && (
                <div className="mt-4 p-4 border rounded bg-gray-50">
                    <input
                        type="text"
                        placeholder="Collection Name"
                        value={newCollectionName}
                        onChange={(e) => setNewCollectionName(e.target.value)}
                        className="border px-2 py-1 w-3/4"
                    />
                    <button
                        onClick={handleAddCollection}
                        className="ml-2 bg-green-500 text-white px-4 py-1 rounded hover:bg-green-600"
                    >
                        Create
                    </button>
                </div>
            )}

            {/* Recipe Selector */}
            {showRecipeSelector && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg max-w-md w-full">
                        <h2 className="text-xl font-bold mb-4">Add Recipe to Collection</h2>
                        <ul className="space-y-2 max-h-60 overflow-y-auto">
                            {recipes.map(recipe => (
                                <li key={recipe.id} className="flex justify-between items-center">
                                    <span>{recipe.name}</span>
                                    <button
                                        onClick={() => handleAddRecipeToCollection(recipe.id)}
                                        className="ml-2 bg-blue-500 text-white px-2 py-1 rounded text-sm"
                                    >
                                        Add
                                    </button>
                                </li>
                            ))}
                        </ul>
                        <button
                            onClick={() => setShowRecipeSelector(false)}
                            className="mt-4 bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                        >
                            Close
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ViewCollections;