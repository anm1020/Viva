document.addEventListener('DOMContentLoaded', function () {
  let isIdChecked = false;

  // 1. 아이디 유효성 + 중복확인 버튼
  const idInput = document.getElementById('userId');
  const idCheckBtn = document.getElementById('checkIdBtn');
  const idCheckResult = document.getElementById('idCheckResult');
  idInput.addEventListener('input', function () {
    isIdChecked = false;
    const val = this.value.trim();
    if (!val) {
      idCheckResult.textContent = "아이디를 입력하세요! (공백 불가)";
      idCheckResult.style.color = "red";
    } else if (!/^[a-zA-Z0-9]{1,10}$/.test(val)) {
      idCheckResult.textContent = "아이디는 영문 또는 영문+숫자, 10자리 이내! (공백 불가)";
      idCheckResult.style.color = "red";
    } else {
      idCheckResult.textContent = "";
    }
  });

  idCheckBtn.addEventListener('click', function () {
    const userId = idInput.value.trim();
    if (!userId) {
      idCheckResult.textContent = "아이디를 입력하세요! (공백 불가)";
      idCheckResult.style.color = "red";
      idInput.focus();
      isIdChecked = false;
      return;
    }
    if (!/^[a-zA-Z0-9]{1,10}$/.test(userId)) {
      idCheckResult.textContent = "아이디는 영문 또는 영문+숫자, 10자리 이내! (공백 불가)";
      idCheckResult.style.color = "red";
      idInput.focus();
      isIdChecked = false;
      return;
    }
    fetch('/member/checkId?userId=' + encodeURIComponent(userId))
      .then(res => res.text())
      .then(data => {
        if (data === 'ok' || data === 'OK') {
          idCheckResult.textContent = "사용 가능한 아이디입니다.";
          idCheckResult.style.color = "green";
          isIdChecked = true;
        } else {
          idCheckResult.textContent = "이미 사용 중인 아이디입니다.";
          idCheckResult.style.color = "red";
          idInput.focus();
          isIdChecked = false;
        }
      })
      .catch(() => {
        idCheckResult.textContent = "서버 오류!";
        idCheckResult.style.color = "red";
        idInput.focus();
        isIdChecked = false;
      });
  });

  // 2. 비밀번호, 확인 (공백 및 유효성)
  function checkPass() {
    const pass = document.getElementById('userPass').value;
    const pass2 = document.getElementById('userPassConfirm').value;
    const result = document.getElementById('passCheckResult');
    if (!pass) {
      result.textContent = "비밀번호를 입력하세요! (공백 불가)";
      result.style.color = "red";
    } else if (!/^[a-zA-Z0-9!@#$%^&*]{4,10}$/.test(pass)) {
      result.textContent = "비밀번호: 4~10자, 영문+숫자/특수문자 허용 (공백 불가)";
      result.style.color = "red";
    } else if (/\s/.test(pass)) {
      result.textContent = "비밀번호에 공백은 사용할 수 없습니다!";
      result.style.color = "red";
    } else if (pass2 && pass !== pass2) {
      result.textContent = "비밀번호가 일치하지 않습니다.";
      result.style.color = "red";
    } else if (pass2 && pass === pass2) {
      result.textContent = "비밀번호가 일치합니다.";
      result.style.color = "green";
    } else {
      result.textContent = "";
    }
  }
  document.getElementById('userPass').addEventListener('input', checkPass);
  document.getElementById('userPassConfirm').addEventListener('input', checkPass);

  // 3. 이름 한글만 (공백불가)
  const nameInput = document.getElementById('userName');
  const nameCheckResult = document.getElementById('nameCheckResult');
  nameInput.addEventListener('input', function () {
    const val = this.value.trim();
    if (!val) {
      nameCheckResult.textContent = "이름을 입력하세요! (공백 불가)";
      nameCheckResult.style.color = "red";
    } else if (!/^[가-힣]{1,}$/.test(val)) {
      nameCheckResult.textContent = "이름은 한글만 입력! (공백 불가)";
      nameCheckResult.style.color = "red";
    } else {
      nameCheckResult.textContent = "";
    }
  });

  // 4. 생년월일 (필수)
  const birthInput = document.getElementById('userBirth');
  birthInput.addEventListener('blur', function () {
    if (!this.value.trim()) {
      if (!document.getElementById('birthCheckResult')) {
        const s = document.createElement('span');
        s.id = "birthCheckResult";
        s.style.color = "red";
        s.textContent = "생년월일을 입력하세요!";
        this.parentNode.appendChild(s);
      } else {
        document.getElementById('birthCheckResult').textContent = "생년월일을 입력하세요!";
      }
    } else if (document.getElementById('birthCheckResult')) {
      document.getElementById('birthCheckResult').textContent = "";
    }
  });

  // 5. 성별 (필수)
  document.querySelectorAll('input[name="userGender"]').forEach(function (radio) {
    radio.addEventListener('change', function () {
      document.getElementById('genderCheckResult').textContent = "";
    });
  });

  // 6. 이메일 id: 영문/숫자만 (공백불가)
  const emailId = document.getElementById('emailId');
  const emailCheckResult = document.getElementById('emailCheckResult');
  emailId.addEventListener('input', function () {
    const val = this.value.trim();
    if (!val) {
      emailCheckResult.textContent = "이메일 아이디를 입력하세요! (공백 불가)";
      emailCheckResult.style.color = "red";
    } else if (!/^[a-zA-Z0-9]+$/.test(val)) {
      emailCheckResult.textContent = "영문/숫자만 입력! (공백 불가)";
      emailCheckResult.style.color = "red";
    } else {
      emailCheckResult.textContent = "";
    }
  });

  // 7. 이메일 도메인: select로 선택 or 직접입력 (공백불가)
  const domainSelect = document.getElementById('emailDomainSelect');
  const domainInput = document.getElementById('emailDomain');
  domainSelect.addEventListener('change', function () {
    if (this.value === 'custom') {
      domainInput.value = '';
      domainInput.removeAttribute('readonly');
      domainInput.focus();
    } else if (this.value) {
      domainInput.value = this.value;
      domainInput.setAttribute('readonly', 'readonly');
    } else {
      domainInput.value = '';
      domainInput.setAttribute('readonly', 'readonly');
    }
  });
  domainInput.addEventListener('input', function() {
    if (!this.value.trim()) {
      emailCheckResult.textContent = "이메일 도메인을 입력하거나 선택하세요! (공백 불가)";
      emailCheckResult.style.color = "red";
    } else {
      emailCheckResult.textContent = "";
    }
  });

  // 8. 핸드폰번호 (중간, 끝 4자리, 공백불가)
  const phoneCheckResult = document.getElementById('phoneCheckResult');
  ['phoneMiddle', 'phoneLast'].forEach(function (id) {
    document.getElementById(id).addEventListener('input', function () {
      const val = this.value.trim();
      if (!val) {
        phoneCheckResult.textContent = "숫자 4자리 입력! (공백 불가)";
        phoneCheckResult.style.color = "red";
      } else if (!/^\d{4}$/.test(val)) {
        phoneCheckResult.textContent = "숫자 4자리 입력!";
        phoneCheckResult.style.color = "red";
      } else {
        phoneCheckResult.textContent = "";
      }
    });
  });

  // 9. 주소: 필수 (공백불가)
  const addressCheckResult = document.getElementById('addressCheckResult');
  document.getElementById('userAdd').addEventListener('input', function () {
    if (!this.value.trim()) {
      addressCheckResult.textContent = "주소를 입력하세요! (공백 불가)";
      addressCheckResult.style.color = "red";
    } else {
      addressCheckResult.textContent = "";
    }
  });

  // 10. 상세주소: 영문/숫자/한글, 공백만 입력 불가
  const detailInput = document.getElementById('userAddDetail');
  const addressDetailCheckResult = document.getElementById('addressDetailCheckResult');
  detailInput.addEventListener('input', function () {
    const val = this.value.trim();
    if (this.value && !val) {
      addressDetailCheckResult.textContent = "상세주소를 입력하세요! (공백만 불가)";
      addressDetailCheckResult.style.color = "red";
    } else if (this.value && !/^[가-힣a-zA-Z0-9\s]+$/.test(val)) {
      addressDetailCheckResult.textContent = "문자/숫자만 입력!";
      addressDetailCheckResult.style.color = "red";
    } else {
      addressDetailCheckResult.textContent = "";
    }
  });

  // 11. 기술 - 1개 이상 체크
  // 12. 면접관 경력 필수 (공백불가)

  // 13. 폼 제출시 모든 항목 다시 한 번 체크 + 앞뒤공백 제거
  document.querySelector('form').addEventListener('submit', function (e) {
    // (1) 앞뒤공백 자동 제거
    this.querySelectorAll('input[type="text"], input[type="password"], textarea').forEach(function(input){
      if (input.value) input.value = input.value.trim();
    });
    // (2) 중간 공백 금지 체크는 각 항목별 정규식에서 처리

    // 1. 기술체크
    const skills = document.querySelectorAll('input[name="skill"]:checked');
    if (skills.length < 1) {
      alert("기술은 1개 이상 선택하세요!");
      document.querySelector('input[name="skill"]').focus();
      e.preventDefault();
      return;
    }
    // 2. 아이디
    const userId = idInput.value.trim();
    if (!userId) {
      alert("아이디를 입력하세요. (공백 불가)");
      idInput.focus();
      e.preventDefault();
      return;
    }
    if (!/^[a-zA-Z0-9]{1,10}$/.test(userId)) {
      alert("아이디는 영문 또는 영문+숫자, 10자리 이내여야 합니다.");
      idInput.focus();
      e.preventDefault();
      return;
    }
    if (!isIdChecked) {
      alert("아이디 중복확인을 해주세요!");
      idCheckBtn.focus();
      e.preventDefault();
      return;
    }
    // 3. 비밀번호
    const pass = document.getElementById('userPass').value;
    const pass2 = document.getElementById('userPassConfirm').value;
    if (!pass) {
      alert("비밀번호를 입력하세요! (공백 불가)");
      document.getElementById('userPass').focus();
      e.preventDefault();
      return;
    }
    if (!/^[a-zA-Z0-9!@#$%^&*]{4,10}$/.test(pass) || /\s/.test(pass)) {
      alert("비밀번호는 4~10자, 영문+숫자/특수문자 허용 (공백 불가) 입니다.");
      document.getElementById('userPass').focus();
      e.preventDefault();
      return;
    }
    if (pass !== pass2) {
      alert("비밀번호가 일치하지 않습니다.");
      document.getElementById('userPassConfirm').focus();
      e.preventDefault();
      return;
    }
    // 4. 이름
    if (!nameInput.value.trim()) {
      alert("이름을 입력하세요! (공백 불가)");
      nameInput.focus();
      e.preventDefault();
      return;
    }
    if (!/^[가-힣]{1,}$/.test(nameInput.value.trim())) {
      alert("이름은 한글만 입력하세요.");
      nameInput.focus();
      e.preventDefault();
      return;
    }
    // 5. 생년월일
    if (!birthInput.value.trim()) {
      alert("생년월일을 입력하세요.");
      birthInput.focus();
      e.preventDefault();
      return;
    }
    // 6. 성별
    if (!document.querySelector('input[name="userGender"]:checked')) {
      alert("성별을 선택하세요.");
      document.getElementById('genderCheckResult').textContent = "성별을 선택하세요!";
      document.getElementById('genderCheckResult').style.color = "red";
      document.getElementById('male').focus();
      e.preventDefault();
      return;
    }
    // 7. 이메일
    if (!emailId.value.trim()) {
      alert("이메일 아이디를 입력하세요! (공백 불가)");
      emailId.focus();
      e.preventDefault();
      return;
    }
    if (!/^[a-zA-Z0-9]+$/.test(emailId.value.trim())) {
      alert("이메일 아이디는 영문/숫자만 입력!");
      emailId.focus();
      e.preventDefault();
      return;
    }
    if (!domainInput.value.trim()) {
      alert("이메일 도메인을 입력 또는 선택하세요! (공백 불가)");
      domainInput.focus();
      e.preventDefault();
      return;
    }
    // 8. 핸드폰
    const mid = document.getElementById('phoneMiddle').value.trim();
    const last = document.getElementById('phoneLast').value.trim();
    if (!mid || !last) {
      alert("휴대폰번호는 숫자 4자리씩 입력! (공백 불가)");
      document.getElementById('phoneMiddle').focus();
      e.preventDefault();
      return;
    }
    if (!/^\d{4}$/.test(mid) || !/^\d{4}$/.test(last)) {
      alert("휴대폰번호는 숫자 4자리씩 입력!");
      document.getElementById('phoneMiddle').focus();
      e.preventDefault();
      return;
    }
    // 9. 주소
    if (!document.getElementById('userAdd').value.trim()) {
      alert("주소를 입력하세요! (공백 불가)");
      document.getElementById('userAdd').focus();
      e.preventDefault();
      return;
    }
    // 10. 상세주소
    if (detailInput.value && !detailInput.value.trim()) {
      alert("상세주소를 입력하세요! (공백만 불가)");
      detailInput.focus();
      e.preventDefault();
      return;
    }
    if (detailInput.value && !/^[가-힣a-zA-Z0-9\s]+$/.test(detailInput.value.trim())) {
      alert("상세주소는 문자/숫자만 입력!");
      detailInput.focus();
      e.preventDefault();
      return;
    }
    // 11. 면접관 경력
    const userRole = document.querySelector('[name="userRole"]')?.value;
    if (userRole === 'intr') {
      const career = document.getElementById('userCareer');
      if (!career.value.trim()) {
        alert("경력을 입력하세요! (면접관만, 공백불가)");
        career.focus();
        e.preventDefault();
        return;
      }
    }
  });

  // 카카오 주소 검색
  document.getElementById('addrBtn')?.addEventListener('click', function () {
    new daum.Postcode({
      oncomplete: function (data) {
        document.getElementById('userAdd').value = data.address;
        addressCheckResult.textContent = "";
      }
    }).open();
  });
});
