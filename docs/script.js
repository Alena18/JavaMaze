// BUTTON REFERENCES
const startBtn = document.getElementById("startButton");
const repeatBtn = document.getElementById("repeatButton");
const resultBox = document.getElementById("resultBox");

// START GAME BUTTON
startBtn.addEventListener("click", () => {
  startBtn.disabled = true; // Disable the button to prevent double click
  if (typeof main !== "undefined") {
    main(); // Starts game via TeaVM (Java → JS)
  }
});

// PLAY AGAIN BUTTON
repeatBtn.addEventListener("click", () => {
  startBtn.disabled = true;
  resultBox.classList.remove("visible");
  resultBox.innerText = "";

  if (typeof g_GameCanvas !== "undefined" && g_GameCanvas.endGame) {
    g_GameCanvas.endGame(); // Ensures timer is stopped
  }
  main(); // Restart clean
});

// SHOW RESULT (CALLED FROM JAVA)
function showResult(text) {
  const resultBox = document.getElementById("resultBox");
  resultBox.innerText = text;
  resultBox.classList.remove("hidden");
  resultBox.classList.add("visible");
  // Launch confetti
  launchCustomConfetti(700); // you can adjust the number
}

function resetResultBox() {
  const resultBox = document.getElementById("resultBox");
  resultBox.classList.remove("visible");
  resultBox.classList.add("hidden");
  // Optionally hide again after a while
  setTimeout(() => {
    resultBox.classList.remove("visible");
  }, 10000);
}

// LAUNCH CONFETTI ANIMATION
function launchCustomConfetti(count = 200) {
  const container = document.getElementById("confettiContainer");
  container.innerHTML = "";

  for (let i = 0; i < count; i++) {
    const confetti = document.createElement("div");
    confetti.classList.add("confetti");

    // Random position & color
    confetti.style.left = Math.random() * 100 + "vw";
    confetti.style.backgroundColor = getRandomColor();
    confetti.style.top = Math.random() * -50 + "vh";
    // Slight staggered delay
    confetti.style.animationDelay = Math.random() * 1.5 + "s";

    container.appendChild(confetti);
  }

  // Cleanup after fall
  setTimeout(() => {
    container.innerHTML = "";
  }, 11000); // matches animation duration
}

// GET RANDOM CONFETTI COLOR
function getRandomColor() {
  const colors = ["#e74c3c", "#f1c40f", "#2ecc71", "#3498db", "#9b59b6"];
  return colors[Math.floor(Math.random() * colors.length)];
}

// OPTIONAL: PREVENT ARROW KEY SCROLLING
window.addEventListener(
  "keydown",
  function (e) {
    if (["ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight"].includes(e.key)) {
      e.preventDefault();
    }
  },
  false
);

function movePlayer(direction) {
  const event = new KeyboardEvent("keydown", { key: direction });
  window.dispatchEvent(event);
}
