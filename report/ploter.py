import pandas as pd
import matplotlib.pyplot as plt
import os
import re

# ─── 1. Load & clean data ──────────────────────────────────────────────────────

df = pd.read_csv("result.csv")

# normalize column names
df.columns = (
    df.columns
      .str.strip()
      .str.replace(" ", "_")
      .str.replace("(", "")
      .str.replace(")", "")
      .str.replace(",", ".")
)

# convert Score to float
if df["Score"].dtype == object:
    df["Score"] = (
        df["Score"]
          .str.replace(",", ".", regex=False)
          .astype(float)
    )
else:
    df["Score"] = pd.to_numeric(df["Score"], errors="coerce")

# extract algorithm name from the Benchmark column
df["Algorithm"] = df["Benchmark"].str.extract(r"BenchmarkRunner\.([A-Za-z0-9_]+)")

# convert parameters to numeric
df["Param_size"] = pd.to_numeric(df["Param:_size"], errors="coerce")
df["Param_sparsity"] = pd.to_numeric(df["Param:_sparsity"], errors="coerce")


# ─── 2. Prepare output dir ────────────────────────────────────────────────────

os.makedirs("plots", exist_ok=True)


# ─── 3. Plotting function ─────────────────────────────────────────────────────

def save_grouped_bar_by_sparsity(unit):
    """
    For each unique sparsity level in `Param_sparsity`, plot a grouped bar chart
    of Score vs. Algorithm, with one bar per matrix size.
    """
    print(f"Generating plots for unit: {unit!r}")

    # enforce a fixed algorithm order:
    alg_order  = ["loopUnrolledMultiply", "naiveMultiply", "sparseMultiply", "strassenMultiply"]
    # collect all sizes and sort them
    size_order = sorted(df["Param_size"].dropna().unique())

    for sparsity in sorted(df["Param_sparsity"].dropna().unique()):
        sub = df[(df["Unit"] == unit) & (df["Param_sparsity"] == sparsity)]
        if sub.empty:
            print(f"  skipping sparsity={sparsity}: no data")
            continue

        # pivot so rows=algorithms, cols=sizes
        pivot = sub.pivot_table(
            index="Algorithm",
            columns="Param_size",
            values="Score",
            aggfunc="mean"
        )

        # re-order rows & columns
        pivot = pivot.reindex(index=alg_order, columns=size_order)

        # plot
        fig, ax = plt.subplots(figsize=(8, 6))
        pivot.plot(
            kind="bar",
            width=0.75,
            ax=ax,
            legend=True
        )

        # formatting to match your example
        ax.set_title(f"{unit} at sparsity = {sparsity}")
        ax.set_xlabel("Algorithm")
        ax.set_ylabel(unit)
        ax.set_xticklabels(pivot.index, rotation=0)      # horizontal labels
        ax.legend(title="Matrix Size")
        ax.grid(axis="y", linestyle="--", alpha=0.5)     # light horizontal grid
        ax.set_axisbelow(True)                           # grid below bars
        ax.set_ylim(bottom=0)                            # start y‐axis at 0

        plt.tight_layout()

        # save out
        fname = f"plots/{unit.replace('/', '-')}_sparsity_{str(sparsity).replace('.', '-')}.png"
        fig.savefig(fname, dpi=150)
        plt.close(fig)
        print(f"  Saved: {fname}")


# ─── 4. Run for each unit ─────────────────────────────────────────────────────

for unit in ["ms/op", "MB/sec", "B/op"]:
    save_grouped_bar_by_sparsity(unit)
