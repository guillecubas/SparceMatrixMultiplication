import pandas as pd
import matplotlib.pyplot as plt
import os

# Cargar el CSV
df = pd.read_csv("result.csv")

# Limpiar nombres de columnas
df.columns = df.columns.str.strip().str.replace(" ", "_").str.replace("(", "").str.replace(")", "").str.replace(",", ".")

# Asegurar que 'Score' es float
if df["Score"].dtype == object:
    df["Score"] = df["Score"].str.replace(",", ".", regex=False).astype(float)
else:
    df["Score"] = pd.to_numeric(df["Score"], errors="coerce")

# Extraer algoritmo base (antes de ':' si existe)
df["Algorithm"] = df["Benchmark"].str.extract(r"utils\\.BenchmarkRunner\\.(\w+)")
df["Param_size"] = pd.to_numeric(df["Param:_size"], errors="coerce")
df["Param_sparsity"] = pd.to_numeric(df["Param:_sparsity"], errors="coerce")

# Crear carpeta para guardar las gráficas
os.makedirs("plots", exist_ok=True)

# Función que guarda gráficos de barras agrupadas por sparsity
def save_grouped_bar_by_sparsity(unit):
    values = sorted(df["Param_sparsity"].dropna().unique())
    for sparsity in values:
        sub = df[(df["Unit"] == unit) & (df["Param_sparsity"] == sparsity)]
        if sub.empty:
            continue
        pivot = sub.pivot_table(index="Algorithm", columns="Param_size", values="Score", aggfunc="mean")
        if pivot.empty:
            continue
        ax = pivot.plot(kind="bar", figsize=(8, 6))
        ax.set_title(f"{unit} at sparsity = {sparsity}")
        ax.set_xlabel("Algorithm")
        ax.set_ylabel(unit)
        ax.legend(title="Matrix Size")
        plt.tight_layout()
        fname = f"plots/{unit.replace('/', '-')}_sparsity_{str(sparsity).replace('.', '-')}.png"
        plt.savefig(fname)
        plt.close()

# Ejecutar para ms/op y MB/sec
save_grouped_bar_by_sparsity("ms/op")
save_grouped_bar_by_sparsity("MB/sec")
