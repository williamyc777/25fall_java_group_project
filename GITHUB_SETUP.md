# GitHub Setup Guide

This guide will help you upload your project to GitHub and collaborate with teammates and professors.

## Prerequisites

- Git installed on your computer
- A GitHub account
- GitHub CLI or web access to GitHub

## Step 1: Create a GitHub Repository

### Option A: Using GitHub Website

1. Go to [GitHub.com](https://github.com) and sign in
2. Click the **"+"** icon in the top right corner
3. Select **"New repository"**
4. Fill in the repository details:
   - **Repository name**: `25fall-java` (or your preferred name)
   - **Description**: "NYU Activity Center - Event and Post Management System"
   - **Visibility**: Choose **Private** (for course projects) or **Public**
   - **DO NOT** initialize with README, .gitignore, or license (we already have these)
5. Click **"Create repository"**

### Option B: Using GitHub CLI

```bash
gh repo create 25fall-java --private --description "NYU Activity Center - Event and Post Management System"
```

## Step 2: Initialize Git (if not already done)

If your project is not yet a Git repository:

```bash
cd /Users/mac/25fall-java
git init
```

## Step 3: Configure Git (if not already done)

Set your name and email (if not configured globally):

```bash
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

## Step 4: Check .gitignore

Make sure your `.gitignore` file includes these entries:

```
.idea/
*.iml
target/
backend/h2db/
backend/images/
node_modules/
frontend/node_modules/
.DS_Store
*.log
/tmp/
```

**Important**: The `.gitignore` should exclude:
- Database files (`h2db/`)
- Build artifacts (`target/`)
- IDE files (`.idea/`, `*.iml`)
- Node modules
- Log files

## Step 5: Add Files to Git

```bash
# Add all files (respecting .gitignore)
git add .

# Check what will be committed
git status
```

## Step 6: Create Initial Commit

```bash
git commit -m "Initial commit: NYU Activity Center project"
```

## Step 7: Connect to GitHub Repository

### If you created the repository on GitHub website:

```bash
# Add remote repository (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/25fall-java.git

# Or if using SSH:
git remote add origin git@github.com:YOUR_USERNAME/25fall-java.git
```

### If you used GitHub CLI:

The remote is already added automatically.

## Step 8: Push to GitHub

```bash
# Push to main branch (or master, depending on your default)
git branch -M main
git push -u origin main
```

If prompted for credentials:
- **Username**: Your GitHub username
- **Password**: Use a **Personal Access Token** (not your GitHub password)
  - Create one at: https://github.com/settings/tokens
  - Select scopes: `repo` (full control of private repositories)

## Step 9: Verify Upload

1. Go to your GitHub repository page
2. Verify all files are present
3. Check that sensitive files (database, images) are NOT uploaded

## Collaborating with Teammates

### Adding Collaborators

1. Go to your repository on GitHub
2. Click **Settings** → **Collaborators**
3. Click **"Add people"**
4. Enter teammate's GitHub username or email
5. They will receive an invitation email

### Teammates: Cloning the Repository

Your teammates should run:

```bash
git clone https://github.com/YOUR_USERNAME/25fall-java.git
cd 25fall-java
```

### Working with the Repository

**Pull latest changes:**
```bash
git pull origin main
```

**Make changes and push:**
```bash
git add .
git commit -m "Description of changes"
git push origin main
```

**Create a feature branch:**
```bash
git checkout -b feature-name
# Make changes
git add .
git commit -m "Add feature"
git push origin feature-name
# Create pull request on GitHub
```

## Sharing with Professor

### Option 1: Add as Collaborator

1. Go to repository **Settings** → **Collaborators**
2. Add professor's GitHub username
3. They will have read/write access

### Option 2: Share Repository Link

1. Make sure repository is **Private** (if required)
2. Share the repository URL: `https://github.com/YOUR_USERNAME/25fall-java`
3. Professor can request access or you can add them as collaborator

### Option 3: Create a Release

1. Go to **Releases** → **"Create a new release"**
2. Tag version: `v1.0.0`
3. Release title: "Final Submission"
4. Add release notes
5. Attach any additional documentation

## Best Practices

### Commit Messages

Use clear, descriptive commit messages:
- ✅ Good: `"Fix date format parsing in event creation"`
- ❌ Bad: `"fix"` or `"update"`

### Branch Strategy

- `main`: Stable, production-ready code
- `develop`: Development branch
- `feature/feature-name`: Feature branches
- `fix/bug-name`: Bug fix branches

### Regular Commits

Commit frequently with meaningful messages:
```bash
git add .
git commit -m "Add user authentication"
git push origin main
```

### Before Pushing

Always check what you're committing:
```bash
git status
git diff  # See changes
```

## Troubleshooting

### Authentication Issues

**If push fails with authentication error:**

1. Use Personal Access Token instead of password
2. Or set up SSH keys:
   ```bash
   ssh-keygen -t ed25519 -C "your.email@example.com"
   # Add public key to GitHub: Settings → SSH and GPG keys
   ```

### Large Files

**If you accidentally committed large files:**

```bash
# Remove from Git (but keep locally)
git rm --cached backend/h2db/testdb.mv.db
git commit -m "Remove database file from Git"
git push origin main
```

### Merge Conflicts

**If you have conflicts when pulling:**

```bash
git pull origin main
# Resolve conflicts in files
git add .
git commit -m "Resolve merge conflicts"
git push origin main
```

## Security Notes

⚠️ **Important**: Before pushing, ensure:

1. ✅ No passwords in code (use environment variables)
2. ✅ No database files committed
3. ✅ `.gitignore` is properly configured
4. ✅ No sensitive API keys in code

If you accidentally committed sensitive data:
1. Remove it from Git history (use `git filter-branch` or BFG Repo-Cleaner)
2. Change the exposed credentials immediately

## Additional Resources

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [GitHub CLI Documentation](https://cli.github.com/manual/)

---

**Need Help?** Contact your team lead or refer to GitHub documentation.

