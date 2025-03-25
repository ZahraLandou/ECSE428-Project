import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import RecipeRating from '../components/RecipeRating';

const Recipe = () => {
    const { recipeId } = useParams();
    const userId = 1;

    const [averageRating, setAverageRating] = useState(null);
    const [ratingCount, setRatingCount] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const avgRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/average`);
                const countRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/count`);
                setAverageRating(avgRes.data);
                setRatingCount(countRes.data);
            } catch (err) {
                console.error("Error fetching rating data:", err);
            }
        };

        fetchData();
    }, [recipeId]);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-4"> Recipe #{recipeId}</h1>

            {userId ? (
                <RecipeRating recipeId={recipeId} userId={userId} />
            ) : (
                <p className="text-red-500">You must be logged in to rate this recipe.</p>
            )}
        </div>
    );
};

export default Recipe;
