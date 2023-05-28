document.querySelectorAll('li > a').forEach((nav) => {
  let startPath = "/conferences/";
  let index = "/conferences/index.jsp";
  if (nav.pathname === window.location.pathname ||
      window.location.pathname === startPath && nav.pathname === index) {
    nav.classList.add('active')
  }
})