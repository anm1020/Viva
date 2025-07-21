// 이메일 도메인 선택 이벤트
function bindEmailDomainEvents() {
  const domainSelect = document.getElementById('emailDomainSelect');
  const domainInput  = document.getElementById('emailDomain');
  if (!domainSelect || !domainInput) {
    // console.error('input/select id 확인 필요!');
    return;
  }
  domainSelect.addEventListener('change', function() {
    const val = this.value;
    if (val === 'custom') {
      domainInput.value = '';
      domainInput.removeAttribute('readonly');
      domainInput.focus();
    } else if (val) {
      domainInput.value = val;
      domainInput.setAttribute('readonly', '');
    } else {
      domainInput.value = '';
      domainInput.setAttribute('readonly', '');
    }
  });
}

// [1] 정적폼 진입 시 이메일 도메인 이벤트 바인딩
document.addEventListener('DOMContentLoaded', function() {
  bindEmailDomainEvents();

  // [2] 주소 검색(카카오 우편번호) 버튼 이벤트
  

/*  const addrBtn = document.getElementById('addrBtn'); //초기화
  if (addrBtn) {
    addrBtn.addEventListener('click', function() {
		console.log('addrBtn');
      new daum.Postcode({
        oncomplete: function(data) {
          document.getElementById('userAdd').value = data.address; // 주소 입력
        }
      }).open();
    });
  }*/
});

// [3] AJAX로 동적 삽입(수정폼 등) 이후에는 아래를 추가로 호출해야함
// $('#editContainer').load(..., function(){ bindEmailDomainEvents(); });

// [4] 비밀번호 유효성 검사 (4~10자, 영문/숫자/특수문자, 공백 불가)
function validatePassword(pw) {
  const reg = /^[A-Za-z0-9!@#$%^&*()_\-+=\[\]{};:'",.<>/?\\|]{4,10}$/;
  return reg.test(pw);
}

// [5] 비밀번호 변경 모달 제어
function openPwModal() {
  document.getElementById('pwModal').style.display = 'flex';
}
function closePwModal() {
  document.getElementById('pwModal').style.display = 'none';
  document.getElementById('pwChangeForm').reset();
}

// [6] 비밀번호 변경시 유효성 검사 적용!
function applyNewPassword() {
  const currentPw = document.querySelector('#pwChangeForm input[name="currentPass"]').value;
  const newPw = document.querySelector('#pwChangeForm input[name="changePass"]').value;
  const confirmPw = document.querySelector('#pwChangeForm input[name="confirmChangePass"]').value;
  const msgEl = document.getElementById('pwModalMsg');

  msgEl.textContent = '';

  // ✅ 비밀번호 유효성 체크
  if (!validatePassword(newPw)) {
    msgEl.textContent = '비밀번호는 4~10자, 영문/숫자/특수문자만 가능, 공백 불가';
    return false;
  }

  // 새 비번 불일치 체크
  if (newPw !== confirmPw) {
    msgEl.textContent = '새 비밀번호가 일치하지 않습니다!';
    return false;
  }
  if (!newPw) {
    msgEl.textContent = '새 비밀번호를 입력하세요!';
    return false;
  }

  // ★ 현재 비밀번호 서버에 ajax로 체크!
  $.ajax({
    url: '/checkPassword',
    type: 'POST',
    data: { currentPass: currentPw },
    success: function(isCorrect) {
      if (!isCorrect) {
        msgEl.textContent = '현재 비밀번호가 일치하지 않습니다!';
        return;
      }
      // 모두 통과 → 히든값 복사 & 팝업 닫기
      document.getElementById('changePass').value = newPw;
      document.getElementById('currentPassHidden').value = currentPw;
      closePwModal();
      // 성공시 메시지는 정보수정 submit 후 처리
    },
    error: function() {
      msgEl.textContent = '비밀번호 확인 중 오류가 발생했습니다!';
    }
  });

  return false;
}
