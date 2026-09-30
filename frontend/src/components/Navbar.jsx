function Navbar() {
    return (
        <nav className="navbar">
            <div className="navbar-logo">
                InterviewPrep
            </div>

            <div className="navbar-links">
                <a href="/">Home</a>
                <a href="/courses">Courses</a>
                <a href="/practice">Practice</a>
                <a href="/about">About</a>
            </div>

            <button className="login-button">
                Login
            </button>
        </nav>
    );
}

export default Navbar;