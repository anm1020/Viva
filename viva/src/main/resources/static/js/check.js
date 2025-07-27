
document.addEventListener('DOMContentLoaded', function(){

  // 1. 아이디: 영어+숫자만, 중복검사
  document.getElementById('userId').addEventListener('input', function(){
    const val = this.value;
    const reg = /^[a-zA-Z0-9]+$/;
    if(val && !reg.test(val)) {
      document.getElementById('idCheckResult').style.color = "red";
      document.getElementById('idCheckResult').textContent = "아이디는 영문/숫자만 입력!";
    } else {
      document.getElementById('idCheckResult').textContent = "";
    }
  });

  document.getElementById('checkIdBtn').addEventListener('click', function(){
    const userId = document.getElementById('userId').value.trim();
    if(!userId){
      document.getElementById('idCheckResult').style.color = "red";
      document.getElementById('idCheckResult').textContent = "아이디를 입력하세요.";
      return;
    }
    // 유효성 통과 후 중복체크
    const reg = /^[a-zA-Z0-9]+$/;
    if(!reg.test(userId)){
      document.getElementById('idCheckResult').style.color = "red";
      document.getElementById('idCheckResult').textContent = "아이디는 영문/숫자만 입력!";
      return;
    }
    // AJAX (fetch)로 서버에 중복체크 요청
    fetch('/check-id?userId=' + encodeURIComponent(userId))
      .then(res => res.text())
      .then(data => {
        if(data === 'duplicated'){
          document.getElementById('idCheckResult').style.color = "red";
          document.getElementById('idCheckResult').textContent = "이미 사용중인 아이디입니다.";
        } else {
          document.getElementById('idCheckResult').style.color = "green";
          document.getElementById('idCheckResult').textContent = "사용 가능한 아이디입니다.";
        }
      })
      .catch(err => {
        document.getElementById('idCheckResult').style.color = "red";
        document.getElementById('idCheckResult').textContent = "서버오류";
      });
  });

  // 2. 비밀번호 일치확인
  function checkPass() {
    const pass = document.getElementById('userPass').value;
    const pass2 = document.getElementById('userPassConfirm').value;
    const $r = document.getElementById('passCheckResult');
    if(!pass2){
      $r.textContent = "";
    } else if(pass === pass2){
      $r.style.color = "green";
      $r.textContent = "비밀번호가 일치합니다.";
    } else {
      $r.style.color = "red";
      $r.textContent = "비밀번호가 일치하지 않습니다.";
    }
  }
  document.getElementById('userPass').addEventListener('input', checkPass);
  document.getElementById('userPassConfirm').addEventListener('input', checkPass);

  // 3. 이름 한글만
  document.getElementById('userName').addEventListener('input', function(){
    const val = this.value;
    const reg = /^[가-힣]+$/;
    if(val && !reg.test(val)) {
      document.getElementById('nameCheckResult').style.color = "red";
      document.getElementById('nameCheckResult').textContent = "이름은 한글만 입력!";
    } else {
      document.getElementById('nameCheckResult').textContent = "";
    }
  });

  // 4. 이메일 아이디 영어/숫자만
  document.getElementById('emailId').addEventListener('input', function(){
    const val = this.value;
    const reg = /^[a-zA-Z0-9]+$/;
    if(val && !reg.test(val)){
      document.getElementById('emailCheckResult').style.color = "red";
      document.getElementById('emailCheckResult').textContent = "이메일 아이디는 영문/숫자만 입력!";
    } else {
      document.getElementById('emailCheckResult').textContent = "";
    }
  });

  document.addEventListener('DOMContentLoaded', function() {
    // 1. 성별 실시간 안내
    document.querySelectorAll('input[name="userGender"]').forEach(function(radio) {
      radio.addEventListener('change', function() {
        const checked = document.querySelector('input[name="userGender"]:checked');
        const $r = document.getElementById('genderCheckResult');
        $r.textContent = checked ? "" : "성별을 선택하세요!";
        $r.style.color = checked ? "" : "red";
      });
    });

    // 2. 주소 실시간 안내
    document.getElementById('userAdd').addEventListener('input', function(){
      const $r = document.getElementById('addressCheckResult');
      if(!this.value.trim()) {
        $r.style.color = "red";
        $r.textContent = "주소를 입력하세요!";
      } else {
        $r.textContent = "";
      }
    });

    // 3. 상세주소 실시간 안내
    document.getElementById('userAddDetail').addEventListener('input', function(){
      const $r = document.getElementById('addressDetailCheckResult');
      if(!this.value.trim()) {
        $r.style.color = "red";
        $r.textContent = "상세주소를 입력하세요!";
      } else {
        $r.textContent = "";
      }
    });

    // 4. 휴대폰 실시간 안내
    function checkPhone() {
      const mid = document.getElementById('phoneMiddle').value.trim();
      const last = document.getElementById('phoneLast').value.trim();
      const $r = document.getElementById('phoneCheckResult');
      if (!mid || !last) {
        $r.style.color = "red";
        $r.textContent = "휴대폰번호를 모두 입력하세요!";
      } else if (!/^\d{3,4}$/.test(mid) || !/^\d{4}$/.test(last)) {
        $r.style.color = "red";
        $r.textContent = "번호 형식이 올바르지 않습니다!";
      } else {
        $r.textContent = "";
      }
    }
    document.getElementById('phoneMiddle').addEventListener('input', checkPhone);
    document.getElementById('phoneLast').addEventListener('input', checkPhone);

    // 5. 이메일 도메인 안내
    document.getElementById('emailDomainSelect').addEventListener('change', function(){
      const domain = this.value;
      const $r = document.getElementById('emailDomainCheckResult');
      if(!domain) {
        $r.style.color = "red";
        $r.textContent = "도메인을 선택하세요!";
      } else if(domain === "custom") {
        $r.textContent = "도메인을 직접 입력하세요!";
        $r.style.color = "blue";
      } else {
        $r.textContent = "";
      }
    });
   });

  
});

