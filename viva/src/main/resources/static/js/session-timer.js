// ===== Idle(활동 없는 시간) 체크 타이머 ===== //
let idleTimer = null;
let idle = true;           // true면 Idle상태(움직임X)
let sessionSec = 3600;     // 세션 총 시간(초) - application.properties 값과 맞추기!

function updateSessionDisplay() {
    let min = Math.floor(sessionSec / 60);
    let sec = sessionSec % 60;
    document.getElementById('session-time-val').textContent = `${min}분 ${sec}초`;
}

function startSessionTimer() {
    // 1초마다 세션 시간 줄이기 (활동 중일 때만)
    idleTimer = setInterval(function() {
        if (!idle) {
            sessionSec--;
            updateSessionDisplay();
            if (sessionSec <= 0) {
                clearInterval(idleTimer);
                alert("세션이 만료되었습니다. 다시 로그인해주세요.");
                window.location.href = "/logout"; // 실제 로그아웃 경로로 변경
            }
        }
    }, 1000);
}

function resetIdle() {
    idle = false;
    // 활동이 감지되면 10초 후 다시 idle = true
    if (window.idleResetTimeout) clearTimeout(window.idleResetTimeout);
    window.idleResetTimeout = setTimeout(function() {
        idle = true;
    }, 10000); // 10초 동안 아무 활동 없으면 다시 idle
}

// [필수] DOM 준비되면 타이머 시작
document.addEventListener("DOMContentLoaded", function(){
    updateSessionDisplay();
    startSessionTimer();
});

// [필수] 사용자 활동 감지 시 idle 해제
["mousemove", "keydown", "click"].forEach(function(event){
    document.addEventListener(event, resetIdle, false);
});
