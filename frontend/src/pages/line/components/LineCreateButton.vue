<template>
  <Dialog :close="close" :width="500">
    <template slot="trigger">
      <v-btn
          class="mx-2 line-create-button"
          color="amber"
          depressed
          fab
          @click="initAllStationsView"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
    <template slot="title">
      <div class="width-100 text-center mt-6">노선 생성</div>
    </template>
    <template slot="text">
      <v-form ref="lineForm" v-model="valid" @submit.prevent>
        <v-text-field
            v-model="lineForm.name"
            :rules="rules.line.name"
            color="grey darken-1"
            label="노선 이름"
            outlined
            placeholder="노선 이름"
        ></v-text-field>
        <div class="d-flex">
          <v-select
              v-model="lineForm.upStationId"
              :items="allStationsView"
              class="pr-4"
              color="grey darken-1"
              dense
              item-color="amber darken-3"
              label="상행 종점"
              outlined
              width="400"
          ></v-select>
          <v-icon class="relative arrow-left-right-icon"
          >mdi-arrow-left-right-bold
          </v-icon
          >
          <v-select
              v-model="lineForm.downStationId"
              :items="allStationsView"
              class="pl-4"
              color="grey darken-1"
              dense
              item-color="amber darken-3"
              label="하행 종점"
              outlined
              width="400"
          ></v-select>
        </div>
        <div class="d-flex">
          <v-text-field
              v-model="lineForm.distance"
              color="grey darken-1"
              label="거리"
              outlined
              placeholder="거리"
              type="number"
          ></v-text-field>
        </div>
        <div class="d-flex">
          <v-text-field
              v-model="lineForm.extraFare"
              color="grey darken-1"
              label="추가 요금"
              outlined
              placeholder="(선택) 추가 요금"
          ></v-text-field>
        </div>
        <div>
          <v-text-field
              v-model="lineForm.color"
              :rules="rules.line.color"
              :value="lineForm.color"
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
          @click.prevent="onCreateLine(lineForm)"
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
import shortid from "shortid";
import {SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import validator from "../../../utils/validator";

export default {
  name: "LineCreateButton",
  components: {Dialog},
  mixins: [dialog],
  computed: {
    ...mapGetters(["stations", "lines"]),
  },
  created() {
    this.lineColors = LINE_COLORS.map((color) => {
      return {
        _id: shortid.generate(),
        color,
      };
    });
  },
  methods: {
    ...mapMutations([SET_LINES, SHOW_SNACKBAR]),
    setLineColor(color) {
      this.lineForm.color = color;
    },
    isValid() {
      return this.$refs.lineForm.validate();
    },
    async onCreateLine() {
      if (!this.isValid()) {
        return;
      }
      try {
        const {name, color, upStationId, downStationId, distance} = this.lineForm
        const response = await fetch("/api/lines", {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({name, color, upStationId, downStationId, distance})
        })
        if (!response.ok) {
          throw new Error(`${response.status}`);
        }

        const newLine = await response.json()
        this.setLines([...this.lines, {...newLine}]); //setLines는 데이터를 관리하기 위해 단 1개 존재하는 저장소에 노선 정보를 저장하는 메서드입니다.
        this.initLineForm();
        this.closeDialog();
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.CREATE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.CREATE.FAIL);
        throw new Error(e);
      }
    },
    initLineForm() {
      this.lineForm = {
        name: "",
        color: "",
        upStationId: "",
        downStationId: "",
        distance: "",
        extraFare: "",
      };
      this.$refs.lineForm.resetValidation();
    },
    initAllStationsView() {
      try {
        if (this.stations.length < 1) {
          return;
        }
        this.allStationsView = this.stations.map((station) => {
          return {
            text: station.name,
            value: station.id,
          };
        });
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
      }
    },
  },
  data() {
    return {
      rules: {...validator},
      lineForm: {
        name: "",
        color: "",
        upStationId: "",
        downStationId: "",
        distance: "",
        extraFare: "",
      },
      valid: false,
      lineColors: [...LINE_COLORS],
      allStationsView: [],
    };
  },
};
</script>

<style lang="scss" scoped>
.arrow-left-right-icon {
  bottom: 15px;
}

.line-create-button {
  top: -29px;
}
</style>
