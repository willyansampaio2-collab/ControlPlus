import { StyleSheet, Text, View } from "react-native";
import Svg, { Circle } from "react-native-svg";

export default function ProgressCard() {
  const size = 280;
  const strokeWidth = 16;

  const radius = (size - strokeWidth) / 2;

  const circumference = radius * 2 * Math.PI;

  const progress = 0.82;

  return (
    <View style={styles.container}>
      <Svg width={size} height={size}>
        <Circle
          stroke="#28144E"
          fill="none"
          cx={size / 2}
          cy={size / 2}
          r={radius}
          strokeWidth={strokeWidth}
        />

        <Circle
          stroke="#C77DFF"
          fill="none"
          cx={size / 2}
          cy={size / 2}
          r={radius}
          strokeWidth={strokeWidth}
          strokeDasharray={`${circumference}`}
          strokeDashoffset={circumference * (1 - progress)}
          strokeLinecap="round"
          rotation="-90"
          origin={`${size / 2}, ${size / 2}`}
        />
      </Svg>

      <View style={styles.content}>
        <Text style={styles.label}>DIAS LIVRES</Text>

        <Text style={styles.days}>127</Text>

        <Text style={styles.footer}>SEM O VÍCIO</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginTop: 30,
    alignItems: "center",
    justifyContent: "center",
  },

  content: {
    position: "absolute",
    alignItems: "center",
  },

  label: {
    color: "#C7C7D1",
    letterSpacing: 3,
    fontSize: 14,
    marginBottom: 10,
  },

  days: {
    color: "#FFFFFF",
    fontSize: 72,
    fontWeight: "300",
  },

  footer: {
    color: "#C77DFF",
    fontSize: 18,
    fontWeight: "600",
    marginTop: 10,
  },
});
