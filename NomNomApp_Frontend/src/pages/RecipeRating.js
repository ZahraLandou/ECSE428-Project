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
        <div style={{
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            background: 'linear-gradient(to bottom, #fffbeb, #fed7aa, #fef3c7)',
            width: '100%'
        }}>
            <div style={{
                backgroundColor: 'white',
                padding: '2rem',
                borderRadius: '1rem',
                boxShadow: '0 10px 25px rgba(0, 0, 0, 0.1)',
                textAlign: 'center',
                width: '100%',
                maxWidth: '32rem',
                margin: '0 auto'
            }}>
                <h1 style={{ fontSize: '1.875rem', fontWeight: 'bold', marginBottom: '1rem', color: '#854d0e' }}>Rate this Recipe</h1>
                <p style={{ fontSize: '1.125rem', color: '#1f2937', marginBottom: '1rem' }}>
                    <strong>Average Rating:</strong> {averageRating?.toFixed(1) ?? 'Loading...'} ({ratingCount ?? 0} ratings)
                </p>
                <div style={{ display: 'flex', justifyContent: 'center', gap: '0.75rem', marginBottom: '1.5rem' }}>
                    {[1, 2, 3, 4, 5].map((star) => (
                        <FaStar
                            key={star}
                            size={36}
                            onClick={() => handleStarClick(star)}
                            onMouseEnter={() => setHovered(star)}
                            onMouseLeave={() => setHovered(null)}
                            color={(hovered || selectedRating) >= star ? '#facc15' : '#e4e5e9'}
                            style={{ cursor: 'pointer', transition: 'transform 0.2s', transform: hovered === star ? 'scale(1.1)' : 'scale(1)' }}
                        />
                    ))}
                </div>
                <button
                    onClick={() => navigate(`/recipe/${recipeId}`)}
                    style={{ marginTop: '1rem', fontSize: '0.875rem', color: '#2563eb', cursor: 'pointer' }}
                >
                    ⬅ Back to Recipe
                </button>
            </div>
        </div>
    );
};

export default RecipeRating;