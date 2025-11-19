import { QueryClient } from "@tanstack/react-query";

export const queryClient = new QueryClient();

window.addEventListener("data-updated", () => {
  queryClient.invalidateQueries();
});
