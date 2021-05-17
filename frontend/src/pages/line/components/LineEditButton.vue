<template>
  <Dialog :close="close" :width="500">
    <template slot="trigger">
      <v-btn icon @click="initEditingLine">
        <v-icon color="grey lighten-1">mdi-pencil</v-icon>
      </v-btn>
    </template>
    <template slot="title">
      <div class="width-100 text-center mt-6">노선 수정</div>
    </template>
    <template slot="text">
      <v-form ref="lineEditForm" v-model="valid" @submit.prevent>
        <v-text-field
            v-model="lineEditForm.name"
            :rules="rules.line.name"
            color="grey darken-1"
            label="노선 이름"
            outlined
            placeholder="노선 이름"
        ></v-text-field>
        <div class="d-flex">
          <v-text-field
              v-model="lineEditForm.extraFare"
              color="grey darken-1"
              label="추가 요금"
              outlined
              placeholder="(선택) 추가 요금"
          ></v-text-field>
        </div>
        <div>
          <v-text-field
              v-model="lineEditForm.color"
              :rules="rules.line.color"
              :value="lineEditForm.color"
              disabled
              filled
              label="노선 색상"
          ></v-text-field>
          <p>
            노선의 색상을 아래 팔레트에서 선택해주세요.
          </p>
          <div class="d-flex justify-center">
            <div>
              <template v-for="(option, index) in lineColors">
                <v-btn
                    :key="option._id"
                    :color="option.color"
                    class="color-button ma-1"
                    depressed
                    min-width="30"
                    small
                    @click="setLineColor(option.color)"
                ></v-btn>
                <br
                    v-if="index === 8 || index % 9 === 8"
                    :key="`${option._id}-${index}`"
                />
              </template>
            </div>
          </div>
        </div>
      </v-form>
    </template>
    <template slot="action">
      <v-btn
          :disabled="!valid"
          color="amber"
          depressed
          @click.prevent="onEditLine"
      >확인
      </v-btn
      >
    </template>
  </Dialog>
</template>

<script>
import dialog from "../../../mixins/dialog";
import {mapGetters, mapMutations} from "vuex";
import Dialog from "../../../components/dialogs/Dialog";
import {LINE_COLORS, SNACKBAR_MESSAGES} from "../../../utils/constants";
import {SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import validator from "../../../utils/validator";
import shortid from "shortid";

export default {
  name: "LineEditButton",
  props: {
    line: {
      type: Object,
      required: true,
    },
  },
  components: {Dialog},
  mixins: [dialog],
  computed: {
    ...mapGetters(["lines"]),
  },
  created() {
    this.lineEditForm = {...this.line};
    this.lineColors = LINE_COLORS.map((color) => {
      return {
        _id: shortid.generate(),
        color,
      };
    });
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES]),
    setLineColor(color) {
      this.lineEditForm.color = color;
    },
    initEditingLine() {
      this.lineEditForm = {...this.line};
    },
    async onEditLine() {
      try {
        const lineId = this.line.id
        const {name, color} = this.lineEditForm
        const response = await fetch(`/api/lines/${lineId}`, {
          method: "PUT",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({name, color})
        })
        if (!response.ok) {
          throw new Error(`${response.status}`);
        }
        const lines = await fetch("/api/lines").then(data => data.json())
        this.setLines([...lines])
        this.closeDialog();
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.UPDATE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.UPDATE.FAIL);
        throw new Error(e);
      }
    },
  },
  data() {
    return {
      rules: {...validator},
      lineEditForm: {
        name: "",
        color: "",
        distance: "",
        extraFare: "",
      },
      valid: false,
      lineColors: [...LINE_COLORS],
    };
  },
};
</script>
