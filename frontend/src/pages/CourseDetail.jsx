import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCourseDetail, completeLesson } from "../api/courseApi";

export default function CourseDetail() {
    const { id } = useParams();
    const [course, setCourse] = useState(null);
    const [completedIds, setCompletedIds] = useState(new Set());

    useEffect(() => {
        getCourseDetail(id).then(setCourse);
    }, [id]);

    const handleComplete = async (lessonId) => {
        await completeLesson(lessonId);
        // Update locally instead of refetching the whole course, for instant feedback.
        setCompletedIds((prev) => new Set(prev).add(lessonId));
    };

    if (!course) return <p>Loading...</p>;

    return (
        <div className="page">
            <h2>{course.title}</h2>
            <p>{course.description}</p>
            {course.modules.map((m) => (
                <div className="module-block" key={m.id}>
                    <h3>{m.title}</h3>
                    <ul>
                        {m.lessons.map((l) => (
                            <li key={l.id} className="lesson-row">
                                <div>
                                    <strong>{l.title}</strong>
                                    <p>{l.content}</p>
                                </div>
                                <button
                                    disabled={completedIds.has(l.id)}
                                    onClick={() => handleComplete(l.id)}
                                >
                                    {completedIds.has(l.id) ? "Completed" : "Mark complete"}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}