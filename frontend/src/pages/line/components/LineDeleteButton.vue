<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINES, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";

export default {
  name: "LineDeleteButton",
  props: {
    line: {
      type: Object,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES]),
    async onDeleteLine() {
      try {
        const deleteLineResponse = await fetch(`http://localhost:8080/lines/${this.line.id}`, {
          method: 'DELETE'
        });

        if (!deleteLineResponse.ok) {
          throw new Error(`${deleteLineResponse.status}`);
        }

        const lines = await fetch(
            "http://localhost:8080/lines"
        ).then(lines => {
          if (!lines.ok) {
            throw new Error(`${lines.status}`);
          }
          return lines.json();
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
