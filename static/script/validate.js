const yInput = document.getElementById('y-input');
const submitButton = document.getElementById('submit');

// Функция для валидации вводимых символов
function validateYInput() {
    const yValue = parseFloat(yInput.value);
    // Проверяем, находится ли значение в диапазоне от -3 до 5
    if (yValue <= -3 || yValue >= 5) {
        yInput.classList.add('error');
        submitButton.disabled = true;
    } else {
        yInput.classList.remove('error');
        submitButton.disabled = false;
    }
}

// Функция для фильтрации вводимых символов
function filterInput(event) {
    let yValue = yInput.value;

    // Удаляем все символы, кроме цифр, точки и знака минус
    yValue = yValue.replace(/[^0-9.-]/g, '');

    // Проверяем, чтобы точка была только одна
    const parts = yValue.split('.');
    if (parts.length > 2) {
        yValue = parts[0] + '.' + parts.slice(1).join('');
    }

    yInput.value = yValue;
    validateYInput();
}

yInput.addEventListener('input', filterInput);