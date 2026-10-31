import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { browseCourses, enrollInCourse } from "../api/courseApi";

export default function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState("");

    // Runs once on mount to load the published course list.
    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        setLoading(true);
        const data = await browseCourses();
        setCourses(data);
        setLoading(false);
    };

    const handleEnroll = async (id) => {
        try {
            await enrollInCourse(id);
            setMessage("Enrolled!");
        } catch (err) {
            setMessage(err.response?.data?.error || "Could not enroll");
        }
    };

    if (loading) return <p>Loading courses...</p>;

    return (
        <div className="page">
            <h2>Courses</h2>
            {message && <p className="info">{message}</p>}
            <div className="course-grid">
                {courses.map((c) => (
                    <div className="course-card" key={c.id}>
                        <h3>{c.title}</h3>
                        <p>{c.description}</p>
                        <p className="tag">{c.topic} · {c.difficulty}</p>
                        <div className="card-actions">
                            <Link to={`/courses/${c.id}`}>View</Link>
                            <button onClick={() => handleEnroll(c.id)}>Enroll</button>
                        </div>
                    </div>
                ))}
            </div>
            {courses.length === 0 && <p>No published courses yet.</p>}
        </div>
    );
}