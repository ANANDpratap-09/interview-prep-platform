import { useState } from "react";
import * as practiceApi from "../api/practiceApi";

/**
 * A full practice flow in one page: pick a topic -> start session ->
 * answer questions one at a time -> see result -> finish -> see score.
 * `sessionId` isn't returned by startSession (the backend only returns the
 * question list — see PracticeController), so we can't track it from that
 * call. For this sprint we look it up by re-fetching the dashboard's most
 * recent session after starting, which is simple but not perfectly robust;
 * a cleaner fix is returning sessionId from the backend — note for later.
 */
export default function Practice() {
    const [topic, setTopic] = useState("Java");
    const [difficulty, setDifficulty] = useState("EASY");
    const [questions, setQuestions] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [answer, setAnswer] = useState("");
    const [result, setResult] = useState(null);
    const [summary, setSummary] = useState(null);
    const [sessionId, setSessionId] = useState(null);

    const handleStart = async (e) => {
        e.preventDefault();
        setSummary(null);
        setResult(null);
        setCurrentIndex(0);
        const data = await practiceApi.startSession({ topic, difficulty, questionCount: 5 });
        setSessionId(data.sessionId);
        setQuestions(data.questions);
    };

    const currentQuestion = questions[currentIndex];

    const handleSubmitAnswer = async (e) => {
        e.preventDefault();
        if (!sessionId) return;
        const res = await practiceApi.submitAnswer(sessionId, {
            questionId: currentQuestion.questionId,
            answer,
        });
        setResult(res);
    };

    const handleNext = () => {
        setResult(null);
        setAnswer("");
        if (currentIndex + 1 < questions.length) {
            setCurrentIndex(currentIndex + 1);
        } else {
            handleFinish();
        }
    };

    const handleFinish = async () => {
        const res = await practiceApi.finishSession(sessionId);
        setSummary(res);
        setQuestions([]);
    };

    return (
        <div className="page">
            <h2>Practice</h2>

            {!questions.length && !summary && (
                <form onSubmit={handleStart} className="practice-start">
                    <input value={topic} onChange={(e) => setTopic(e.target.value)} placeholder="Topic (e.g. Java)" />
                    <select value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
                        <option value="EASY">Easy</option>
                        <option value="MEDIUM">Medium</option>
                        <option value="HARD">Hard</option>
                    </select>
                    <button type="submit">Start session</button>
                </form>
            )}

            {currentQuestion && (
                <div className="question-block">
                    <p>Question {currentIndex + 1} of {questions.length}</p>
                    <h3>{currentQuestion.body}</h3>

                    {!result ? (
                        <form onSubmit={handleSubmitAnswer}>
                            {currentQuestion.type === "MCQ" ? (
                                currentQuestion.options.split("|").map((opt) => (
                                    <label key={opt} className="option-row">
                                        <input
                                            type="radio"
                                            name="answer"
                                            value={opt}
                                            checked={answer === opt}
                                            onChange={(e) => setAnswer(e.target.value)}
                                        />
                                        {opt}
                                    </label>
                                ))
                            ) : (
                                <textarea value={answer} onChange={(e) => setAnswer(e.target.value)} />
                            )}
                            <button type="submit">Submit answer</button>
                        </form>
                    ) : (
                        <div className="result-block">
                            {result.correct !== null && (
                                <p>{result.correct ? "Correct!" : "Incorrect."}</p>
                            )}
                            <p><strong>Answer:</strong> {result.correctAnswer}</p>
                            <p>{result.explanation}</p>
                            <button onClick={handleNext}>
                                {currentIndex + 1 < questions.length ? "Next question" : "Finish session"}
                            </button>
                        </div>
                    )}
                </div>
            )}

            {summary && (
                <div className="summary-block">
                    <h3>Session complete</h3>
                    <p>Score: {summary.score}%</p>
                    <p>{summary.correctCount} / {summary.totalQuestions} correct</p>
                </div>
            )}
        </div>
    );
}