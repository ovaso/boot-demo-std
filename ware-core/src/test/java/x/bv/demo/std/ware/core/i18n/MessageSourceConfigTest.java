package x.bv.demo.std.ware.core.i18n;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

class MessageSourceConfigTest {

	private static final ResourceBundle.Control CONTROL = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);

	@Test
	void testLocale() {

		List<String> messageNames = List.of(
			"app_message_zh_CN",
			"app_message",
			"app_message_hints_cn",
			"app_message_hints_en",
			"app_hints_cn",
			"con_cn"
		);

		// System.out.println(longestCommonPrefix(messageNames));
		Map<String, List<String>> groups = groupByPrefix(messageNames);
		groups.forEach((key, value) -> {
			System.out.println("group: " + key);
			value.forEach(System.out::println);
			System.out.println();
		});
	}

	@Test
	void testLocale2() {

		Set<String> localeCodes = new HashSet<>();
		for (Locale locale : Locale.getAvailableLocales()) {
			localeCodes.add(locale.toString());
		}
		List<String> messageNames = List.of(
			"app_message_zh_CN",
			"app_message_en_us",
			"app_message_hints_zh",
			"app_message_hints_en",
			"app_hints_cn",
			"con_cn"
		);
		for (String messageName : messageNames) {
			System.out.println(stripLocale(messageName, localeCodes));
		}
	}

	public static String stripLocale(String filename, Set<String> supportedLocales) {

		for (String locale : supportedLocales) {
			if (filename.endsWith("." + locale)
				|| filename.endsWith("_" + locale)
				|| filename.endsWith("." + locale.toLowerCase())
				|| filename.endsWith("_" + locale.toLowerCase())) {

				return filename.substring(0, filename.length() - locale.length() - 1);
			}
		}
		return filename; // 没有匹配
	}

	// public static Map<String, List<String>> groupByPrefix(List<String> keys) {
	// 	if (keys == null || keys.isEmpty()) return Collections.emptyMap();
	//
	// 	// 先排序
	// 	List<String> sorted = new ArrayList<>(keys);
	// 	Collections.sort(sorted);
	//
	// 	Map<String, List<String>> result = new LinkedHashMap<>();
	// 	String currentPrefix = sorted.get(0);
	// 	List<String> currentGroup = new ArrayList<>();
	// 	currentGroup.add(sorted.get(0));
	//
	// 	for (int i = 1; i < sorted.size(); i++) {
	// 		String s = sorted.get(i);
	// 		String lcp = longestCommonPrefix(Arrays.asList(currentPrefix, s));
	// 		if (!lcp.isEmpty()) {
	// 			currentPrefix = lcp;
	// 			currentGroup.add(s);
	// 		} else {
	// 			// 保存上一组
	// 			result.put(currentPrefix, new ArrayList<>(currentGroup));
	// 			// 开始新组
	// 			currentGroup.clear();
	// 			currentGroup.add(s);
	// 			currentPrefix = s;
	// 		}
	// 	}
	// 	// 保存最后一组
	// 	result.put(currentPrefix, new ArrayList<>(currentGroup));
	// 	return result;
	// }

	private static String longestCommonPrefix(List<String> strs) {

		if (strs == null || strs.isEmpty()) return "";
		String prefix = strs.get(0);
		for (int i = 1; i < strs.size(); i++) {
			while (!strs.get(i).startsWith(prefix)) {
				prefix = prefix.substring(0, prefix.length() - 1);
				if (prefix.isEmpty()) return "";
			}
		}
		return prefix;
	}

	// Trie 节点
	static class TrieNode {
		String key;
		Map<String, TrieNode> children = new LinkedHashMap<>();
		List<String> originals = new ArrayList<>();

		TrieNode(String key) {

			this.key = key;
		}
	}

	// 插入字符串
	private static void insert(TrieNode root, String s, String delimiter) {

		String[] parts = s.split(delimiter);
		TrieNode node = root;
		for (String part : parts) {
			node = node.children.computeIfAbsent(part, TrieNode::new);
		}
		node.originals.add(s);
	}

	// 遍历 Trie，生成分组
	private static void collectGroups(TrieNode node, String path, Map<String, List<String>> groups) {

		if (!node.originals.isEmpty()) {
			groups.put(path, node.originals);
		}
		for (TrieNode child : node.children.values()) {
			String newPath = path.isEmpty() ? child.key : path + "_" + child.key;
			collectGroups(child, newPath, groups);
		}
	}

	public static Map<String, List<String>> groupByPrefix(List<String> keys) {

		TrieNode root = new TrieNode("");
		String delimiter = "_";

		for (String s : keys) {
			insert(root, s, delimiter);
		}

		Map<String, List<String>> groups = new LinkedHashMap<>();
		collectGroups(root, "", groups);
		return groups;
	}
}
