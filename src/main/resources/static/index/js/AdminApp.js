import { initIndexNavigation } from "../../admin/utils/templates.js";

function AdminApp() {
  const init = () => {
    initIndexNavigation();
  };

  return {
    init
  };
}

const adminApp = new AdminApp();
adminApp.init();
