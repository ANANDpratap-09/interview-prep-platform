import { useEffect, useState } from "react";
import { getDashboard } from "../api/progressApi";

export default function Dashboard() {
    const [dashboard, setDashboard] = useState(null);

    useEffect(() => {
        getDashboard().then(setDashboard);
    }, []);

    if (!dashboard) return <p>Loading...</p>;

    return (
        <div className="page">
            <h2>Your dashboard</h2>
            <p>Enrolled in {dashboard.enrolledCourseCount} course(s)</p>

            <h3>Course progress</h3>
            {dashboard.courseProgress.map((cp) => (
                <div key={cp.courseId} className="progress-row">
                    <span>{cp.courseTitle}</span>
                    <div className="progress-bar">
                        <div className="progress-fill" style={{ width: `${cp.completionPercent}%` }} />
                    </div>
                    <span>{cp.completionPercent.toFixed(0)}%</span>
                </div>
            ))}

            <h3>Recent practice sessions</h3>
            {dashboard.recentSessions.map((s) => (
                <div key={s.sessionId} className="session-row">
                    <span>{s.topic}</span>
                    <span>{s.correctCount}/{s.totalQuestions}</span>
                    <span>{s.score}%</span>
                </div>
            ))}

            <h3>Average score by topic</h3>
            {Object.entries(dashboard.averageScoreByTopic).map(([topic, avg]) => (
                <p key={topic}>{topic}: {avg.toFixed(0)}%</p>
            ))}
        </div>
    );
}