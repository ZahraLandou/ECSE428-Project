
import React, { useEffect, useState } from 'react';
import ReactStars from 'react-rating-stars-component';
import axios from 'axios';

const RecipeRating = ({ recipeId, userId }) => {
    const [averageRating, setAverageRating] = useState(0);
    const [ratingCount, setRatingCount] = useState(0);
    const [userRating, setUserRating] = useState(0);

    useEffect(() => {
        const fetchRatingData = async () => {
            try {
                const avgRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/average`);
                const countRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/count`);
                setAverageRating(avgRes.data);
                setRatingCount(countRes.data);
            } catch (err) {
                console.error('Failed to fetch rating data:', err);
            }
        };

        fetchRatingData();
    }, [recipeId]);

    const handleRatingChange = async (newRating) => {
        try {
            await axios.post(`http://localhost:8081/api/ratings/${userId}/${recipeId}?ratingValue=${newRating}`);
            setUserRating(newRating);

            const avgRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/average`);
            const countRes = await axios.get(`http://localhost:8081/api/ratings/${recipeId}/count`);
            setAverageRating(avgRes.data);
            setRatingCount(countRes.data);
        } catch (err) {
            console.error('Rating submit failed:', err);
            alert('You must be logged in to rate this recipe.');
        }
    };

    return (
        <div className="p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-3">⭐ Rate this Recipe</h2>

            <ReactStars
                count={5}
                size={32}
                isHalf={false}
                value={userRating}
                onChange={handleRatingChange}
                activeColor="#ffd700"
            />

            <p className="mt-3 text-lg">
                ⭐ Average Rating: <strong>{averageRating.toFixed(1)}</strong> ({ratingCount} ratings)
            </p>
        </div>
    );
};

export default RecipeRating;
