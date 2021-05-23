<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapGetters, mapMutations} from "vuex";
import {SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "LineDeleteButton",
  props: {
    line: {
      type: Object,
      required: true,
    },
  },
  computed: {
    ...mapGetters(["accessToken"]),
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES]),
    async onDeleteLine() {
      try {
        await fetch(`api/lines/${this.line.id}`, {
          method: "DELETE",
          headers: {
            'Authorization': 'Bearer ' + this.accessToken,
          }
        })
        .then(response => {
          if(!response.ok) {
            throw new Error(`${response.status}`);
          }
        })
        const lines = await fetch("api/lines", {
          headers: {
            'Authorization': 'Bearer ' + this.accessToken,
          }
        })
        .then(response => {
          if (!response.ok) {
            throw new Error(`${response.status}`);
          }
          return response.json();
        })
        this.setLines([...lines])

        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
