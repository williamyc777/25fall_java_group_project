#!/bin/bash
# Script to create a new public repository and push the project

echo "=========================================="
echo "Creating Public Repository for 25fall-java"
echo "=========================================="
echo ""

# Step 1: Remove old remote
echo "Step 1: Removing old remote..."
git remote remove origin 2>/dev/null || echo "No existing remote to remove"
echo "✓ Old remote removed"
echo ""

# Step 2: Add new remote (you need to create the repo on GitHub first)
echo "Step 2: Adding new remote..."
echo "⚠️  IMPORTANT: Please create a new PUBLIC repository on GitHub first!"
echo ""
echo "Go to: https://github.com/new"
echo "Repository name: 25fall-java-public"
echo "Visibility: Public"
echo "DO NOT initialize with README, .gitignore, or license"
echo ""
read -p "Press Enter after you have created the repository on GitHub..."

# Add the new remote
NEW_REPO_URL="https://github.com/williamyc777/25fall-java-public.git"
git remote add origin "$NEW_REPO_URL"
echo "✓ New remote added: $NEW_REPO_URL"
echo ""

# Step 3: Push to new repository
echo "Step 3: Pushing to new repository..."
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ Success! Repository is now public"
    echo "=========================================="
    echo "Repository URL: $NEW_REPO_URL"
else
    echo ""
    echo "=========================================="
    echo "❌ Push failed. Please check:"
    echo "1. Repository exists on GitHub"
    echo "2. Repository is set to Public"
    echo "3. You have push permissions"
    echo "=========================================="
fi

