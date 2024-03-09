package utility;

import java.util.List;

public class BinaryKMP {
	private int offset;

	public boolean kmp(List<Byte> origin, List<Byte> pattern) {
		int[] table = makeTable(pattern);
		int k = 0;
		for (int i = 0; i < origin.size(); i++) {
			while (k > 0 && origin.get(i) != pattern.get(k)) {
				k = table[k - 1];
			}
			if (origin.get(i) == pattern.get(k)) {
				if (k == pattern.size() - 1) {
					//System.out.printf("%d번째에서 찾았습니다.\n", i-pattern.size() + 1);
					offset = origin.size() - (i - pattern.size() + 1);
					//System.out.printf("오프셋은 %d 입니다.\n", offset);
					k = table[k];
					return true;
				} else {
					k++;
				}
			}
		}
		return false;
	}

	public int getOffset() {
		return offset;
	}

	private int[] makeTable(List<Byte> patterns) {
		int[] table = new int[patterns.size()];
		int k = 0;
		for (int i = 1; i < patterns.size(); i++) {
			while (k > 0 && patterns.get(i) != patterns.get(k)) {
				k = table[k - 1];
			}
			if (patterns.get(i) == patterns.get(k)) {
				table[i] = ++k;
			}
		}

		return table;
	}
}
