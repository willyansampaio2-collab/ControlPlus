import { ScrollView, StyleSheet, Text, View } from "react-native";

// Dados mockados — trocar por Firebase quando estiver pronto
const dadosUsuario = {
  sequenciaAtual: 7,
  melhorSequencia: 15,
  totalAtividades: 42,
  mediaXpPorDia: 120,
  metasCumpridas: 28,
  diasAtivo: 35,
};

export default function Stats() {
  const dados = dadosUsuario;

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.titulo}>📊 Estatísticas</Text>

      {/* Sequências */}
      <Text style={styles.secao}>🔥 Sequências</Text>
      <View style={styles.linha}>
        <View style={[styles.card, styles.destaque]}>
          <Text style={styles.cardLabel}>Sequência Atual</Text>
          <Text style={styles.cardValor}>{dados.sequenciaAtual}</Text>
          <Text style={styles.cardUnidade}>dias</Text>
        </View>
        <View style={styles.card}>
          <Text style={styles.cardLabel}>Melhor Sequência</Text>
          <Text style={styles.cardValor}>{dados.melhorSequencia}</Text>
          <Text style={styles.cardUnidade}>dias</Text>
        </View>
      </View>

      {/* Métricas */}
      <Text style={styles.secao}>📈 Métricas</Text>
      <View style={styles.linha}>
        <View style={styles.card}>
          <Text style={styles.cardLabel}>Total de Atividades</Text>
          <Text style={styles.cardValor}>{dados.totalAtividades}</Text>
        </View>
        <View style={styles.card}>
          <Text style={styles.cardLabel}>Média XP/Dia</Text>
          <Text style={styles.cardValor}>{dados.mediaXpPorDia}</Text>
          <Text style={styles.cardUnidade}>XP</Text>
        </View>
      </View>
      <View style={styles.linha}>
        <View style={styles.card}>
          <Text style={styles.cardLabel}>Metas Cumpridas</Text>
          <Text style={styles.cardValor}>{dados.metasCumpridas}</Text>
        </View>
        <View style={styles.card}>
          <Text style={styles.cardLabel}>Dias Ativo</Text>
          <Text style={styles.cardValor}>{dados.diasAtivo}</Text>
          <Text style={styles.cardUnidade}>dias</Text>
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
    padding: 16,
  },
  titulo: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 20,
    color: "#333",
  },
  secao: {
    fontSize: 16,
    fontWeight: "600",
    color: "#666",
    marginBottom: 10,
    marginTop: 8,
  },
  linha: {
    flexDirection: "row",
    gap: 12,
    marginBottom: 12,
  },
  card: {
    flex: 1,
    backgroundColor: "#fff",
    borderRadius: 12,
    padding: 16,
    elevation: 2,
    alignItems: "center",
  },
  destaque: {
    backgroundColor: "#4CAF50",
  },
  cardLabel: {
    fontSize: 12,
    color: "#666",
    marginBottom: 6,
    textAlign: "center",
  },
  cardValor: {
    fontSize: 32,
    fontWeight: "bold",
    color: "#333",
  },
  cardUnidade: {
    fontSize: 12,
    color: "#999",
    marginTop: 2,
  },
});