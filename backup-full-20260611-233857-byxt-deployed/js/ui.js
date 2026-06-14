/* ==========================================
   选题系统 - 移动端 UI 组件
   Toast 提示 + Modal 确认弹窗
   ========================================== */

// ----- Toast -----
function toast(msg, type) {
  type = type || 'info';
  var colors = { success: '#2E7D32', error: '#C62828', info: '#1565C0', warn: '#E65100' };
  var icons = { success: '✓', error: '✕', info: 'ℹ', warn: '⚠' };
  var container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.style.cssText = 'position:fixed;top:60px;left:50%;transform:translateX(-50%);z-index:99999;display:flex;flex-direction:column;gap:8px;pointer-events:none;';
    document.body.appendChild(container);
  }
  var el = document.createElement('div');
  el.style.cssText = 'pointer-events:auto;padding:12px 24px;border-radius:24px;color:#fff;font-size:14px;font-weight:600;box-shadow:0 6px 20px rgba(0,0,0,.15);animation:toastIn .3s ease;text-align:center;max-width:85vw;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;background:' + (colors[type] || colors.info);
  el.textContent = (icons[type] || '') + ' ' + msg;
  container.appendChild(el);
  setTimeout(function () {
    el.style.animation = 'toastOut .25s ease forwards';
    setTimeout(function () { if (el.parentNode) el.parentNode.removeChild(el); }, 250);
  }, 2000);
}
// Add toast animations
(function () {
  var s = document.createElement('style');
  s.textContent = '@keyframes toastIn{from{opacity:0;transform:translateY(-12px)}to{opacity:1;transform:translateY(0)}}@keyframes toastOut{from{opacity:1;transform:translateY(0)}to{opacity:0;transform:translateY(-12px)}}';
  document.head.appendChild(s);
})();

// ----- Modal Confirm -----
function confirmModal(msg, title, onOk, onCancel) {
  title = title || '提示';
  var mask = document.getElementById('modal-mask');
  if (!mask) {
    mask = document.createElement('div');
    mask.id = 'modal-mask';
    mask.style.cssText = 'display:none;position:fixed;inset:0;background:rgba(0,0,0,.45);z-index:99998;justify-content:center;align-items:center;';
    mask.innerHTML =
      '<div class="modal-box" style="background:#fff;border-radius:14px;box-shadow:0 8px 30px rgba(0,0,0,.2);width:88%;max-width:340px;overflow:hidden;animation:modalIn .2s ease">' +
      '<div class="modal-hd" style="padding:18px 20px 12px;font-weight:700;font-size:17px;text-align:center"></div>' +
      '<div class="modal-bd" style="padding:0 20px 16px;text-align:center;font-size:15px;color:#666;line-height:1.6"></div>' +
      '<div class="modal-ft" style="display:flex;border-top:1px solid #f0f0f0">' +
      '<button class="modal-cancel" style="flex:1;height:50px;border:none;background:#fff;font-size:16px;color:#888;cursor:pointer;border-right:1px solid #f0f0f0">取消</button>' +
      '<button class="modal-ok" style="flex:1;height:50px;border:none;background:#fff;font-size:16px;color:#2E7D32;font-weight:600;cursor:pointer">确定</button>' +
      '</div></div>';
    document.body.appendChild(mask);
    var s2 = document.createElement('style');
    s2.textContent = '@keyframes modalIn{from{opacity:0;transform:scale(.9)}to{opacity:1;transform:scale(1)}}';
    document.head.appendChild(s2);
  }
  mask.querySelector('.modal-hd').textContent = title;
  mask.querySelector('.modal-bd').textContent = msg;
  mask.style.display = 'flex';

  var okBtn = mask.querySelector('.modal-ok');
  var cancelBtn = mask.querySelector('.modal-cancel');
  var close = function () { mask.style.display = 'none'; };
  var newOk = okBtn.cloneNode(true);
  var newCancel = cancelBtn.cloneNode(true);
  okBtn.parentNode.replaceChild(newOk, okBtn);
  cancelBtn.parentNode.replaceChild(newCancel, cancelBtn);

  newOk.onclick = function () { close(); if (onOk) onOk(); };
  newCancel.onclick = function () { close(); if (onCancel) onCancel(); };
}
