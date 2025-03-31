import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { FaStar } from 'react-icons/fa';

const RecipeRating = () => {
    const { recipeId } = useParams();
    const navigate = useNavigate();
    const userId = 1;

    const [averageRating, setAverageRating] = useState(null);
    const [ratingCount, setRatingCount] = useState(null);
    const [hovered, setHovered] = useState(null);
    const [selectedRating, setSelectedRating] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [avgRes, countRes] = await Promise.all([
                    axios.get(`http://localhost:8081/api/ratings/${recipeId}/average`),
                    axios.get(`http://localhost:8081/api/ratings/${recipeId}/count`)
                ]);
                setAverageRating(avgRes.data);
                setRatingCount(countRes.data);
            } catch (err) {
                console.error("Error fetching ratings:", err);
            }
        };

        fetchData();
    }, [recipeId]);

    const handleStarClick = async (rating) => {
        setSelectedRating(rating);
        try {
            await axios.post(`http://localhost:8081/api/ratings/${userId}/${recipeId}?ratingValue=${rating}`);
            const [avgRes, countRes] = await Promise.all([
                axios.get(`http://localhost:8081/api/ratings/${recipeId}/average`),
                axios.get(`http://localhost:8081/api/ratings/${recipeId}/count`)
            ]);
            setAverageRating(avgRes.data);
            setRatingCount(countRes.data);
            alert(`Thanks for rating ${rating} stars!`);
            navigate(`/recipe/${recipeId}`);
        } catch (err) {
            console.error("Error submitting rating:", err);
            alert("You must be logged in to rate this recipe.");
        }
    };

    return (
        <div className="p-6 max-w-xl mx-auto">
            <h1 className="text-3xl font-bold mb-4">⭐ Rate this Recipe</h1>

            <p className="text-lg mb-2">
                ⭐ <strong>Average Rating:</strong> {averageRating?.toFixed(1) ?? 'Loading...'} ({ratingCount ?? 0} ratings)
            </p>

            <div className="flex gap-2 mb-4">
                {[1, 2, 3, 4, 5].map((star) => (
                    <FaStar
                        key={star}
                        size={32}
                        onClick={() => handleStarClick(star)}
                        onMouseEnter={() => setHovered(star)}
                        onMouseLeave={() => setHovered(null)}
                        color={(hovered || selectedRating) >= star ? '#ffc107' : '#e4e5e9'}
                        style={{ cursor: 'pointer' }}
                    />
                ))}
            </div>

            <button
                onClick={() => navigate(`/recipe/${recipeId}`)}
                className="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400"
            >
                ⬅ Back to Recipe
            </button>
        </div>
    );
};

export default RecipeRating;
