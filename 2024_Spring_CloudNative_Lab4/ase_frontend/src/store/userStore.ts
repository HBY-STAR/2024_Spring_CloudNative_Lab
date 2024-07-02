import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    username: '',
  }),
  getters: {
    getUsername: (state) => state.username
  },
  actions: {
    setUsername(username: string){
      this.username = username
    }
  },
});
