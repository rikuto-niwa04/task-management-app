# Task Domain Specification（タスク仕様）

本ドキュメントは、本アプリケーションにおける **Task（タスク）ドメインの業務ルール**を定義する。
Task は本システムの中核となるエンティティであり、以降の実装・拡張は本仕様を前提とする。

---

## 1. Task の定義
Task とは、ユーザーが業務上実行・管理する作業単位を指す。

Task は以下の情報を保持する。
- 何をするか（内容）
- 誰がやるか（担当者）
- いつまでにやるか（期限）
- 今どの状態か（進捗）

---

## 2. カラム定義（MVP）

| カラム名 | 型 | NULL | 説明 |
|---|---|---|---|
| id | BIGINT | NO | タスクID |
| title | VARCHAR(120) | NO | タスク名（一覧表示用） |
| description | TEXT | YES | 詳細説明 |
| status | VARCHAR(20) | NO | タスクの状態 |
| due_date | DATE | YES | 期限 |
| assignee_id | BIGINT | YES | 担当ユーザーID |
| created_at | TIMESTAMP | NO | 作成日時 |
| updated_at | TIMESTAMP | NO | 更新日時 |

---

## 3. status 定義

### 使用する status（MVP）
- `TODO`：未着手
- `IN_PROGRESS`：対応中
- `DONE`：完了

---

## 4. 状態遷移ルール

### 許可される遷移
- `TODO` → `IN_PROGRESS`（着手）
- `IN_PROGRESS` → `DONE`（完了）
- `IN_PROGRESS` → `TODO`（差し戻し）
- `DONE` → `IN_PROGRESS`（再開）

### 禁止される遷移
- `TODO` → `DONE`（未着手完了は禁止）

---

## 5. 操作と状態変更の対応

| 操作 | 遷移 |
|---|---|
| 着手 | TODO → IN_PROGRESS |
| 完了 | IN_PROGRESS → DONE |
| 差し戻し | IN_PROGRESS → TODO |
| 再開 | DONE → IN_PROGRESS |

---

## 6. 状態別の編集ルール

### TODO / IN_PROGRESS
以下すべて編集可能とする。
- title
- description
- due_date
- assignee_id
- status

### DONE
- title：編集可
- description：編集可
- due_date：編集可
- assignee_id：編集可
- status：**再開操作（DONE → IN_PROGRESS）のみ変更可**

※ 完了後も誤字修正・担当者修正などが発生するため、編集を完全禁止しない。

---

## 7. 作成時ルール
- 作成時の status は必ず `TODO`
- title は必須（空文字・空白のみは不可）
- description / due_date / assignee_id は任意

---

## 8. 削除ルール（MVP）
- Task は物理削除を許可する
- 削除操作は監査ログに記録する

---

## 9. 検索・フィルタ時の扱い
- status：完全一致でフィルタ
- due_date：NULL は「期限なし」として扱う
- assignee_id：NULL は「未割当」として扱う

---

## 10. 監査ログ対象イベント（定義のみ）
以下のイベントを監査ログとして記録する。

- CREATE：タスク作成
- UPDATE：内容更新
- STATUS_CHANGE：状態変更
- DELETE：削除

---

## 11. 本仕様の位置づけ
- 本ドキュメントは Task ドメインの **正とする仕様**である
- 実装は必ず本仕様に従う
- 仕様変更は、本ドキュメントの修正を先に行う
